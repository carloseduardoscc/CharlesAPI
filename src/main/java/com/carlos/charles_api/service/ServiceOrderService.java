package com.carlos.charles_api.service;

import com.carlos.charles_api.dto.request.OpenServiceOrderRequestDTO;
import com.carlos.charles_api.dto.response.ServiceOrderDetailsDTO;
import com.carlos.charles_api.dto.response.ServiceOrderSummaryDTO;
import com.carlos.charles_api.exceptions.BusinessRuleException;
import com.carlos.charles_api.exceptions.ResourceNotFoundException;
import com.carlos.charles_api.infra.pdf.PdfReportGenerator;
import com.carlos.charles_api.model.entity.*;
import com.carlos.charles_api.model.enums.Role;
import com.carlos.charles_api.model.enums.SoStateType;
import com.carlos.charles_api.repository.ServiceOrderRepository;
import com.carlos.charles_api.repository.SoStateRepository;
import com.carlos.charles_api.repository.WorkspaceRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class ServiceOrderService {

    @Autowired
    private ServiceOrderRepository serviceOrderRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PdfReportGenerator pdfReportGenerator;

    @Autowired
    private SoStateRepository soStateRepository;

    private Random random = new Random();

    private static final Logger logger = LoggerFactory.getLogger("ACCESS_LOGGER");

    private WorkspaceRepository workspaceRepository;

    @Transactional
    public Long openNewServiceOrder(OpenServiceOrderRequestDTO soData) {
        User user = userService.getCurrentAuthenticatedUser();
        Workspace workspace = user.getWorkspace();

        if (!List.of(Role.COLLABORATOR, Role.OWNER).contains(user.getRole())) {
            throw new BusinessRuleException("Apenas Collaborators e Owners podem abrir ordem de serviço! O usuário atual tem permissões de " + user.getRole().toString());
        }

        ServiceOrder so = new ServiceOrder(generateSoCode(), soData.description(), workspace, user);
        SoState state = new SoState(null, LocalDateTime.now(), SoStateType.OPEN, so);
        so.getStates().add(state);
        serviceOrderRepository.save(so);
        soStateRepository.save(state);

        logger.atInfo().log("Ordem de serviço {} aberta pelo usuário " +
                "{} com sucesso!", so.getSoCode(), user.getIdentification());

        return so.getId();
    }

    @Transactional
    public List<ServiceOrderSummaryDTO> listServiceOrders() {
        User user = userService.getCurrentAuthenticatedUser();
        Workspace workspace = user.getWorkspace();

        List<ServiceOrder> serviceOrders;
        if (user.getRole().equals(Role.COLLABORATOR)) {
            serviceOrders = serviceOrderRepository.findByWorkspaceIdAndSolicitantId(workspace.getId(), user.getId());
        } else {
            serviceOrders = serviceOrderRepository.findByWorkspaceId(workspace.getId());
        }

        logger.atInfo().log("User {} buscou ordens de serviço no workspace {}", user.getIdentification(), workspace.getIdentification());

        return serviceOrders.stream()
                .map(ServiceOrderSummaryDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public ServiceOrderDetailsDTO serviceOrderDetails(Long id) {
        User user = userService.getCurrentAuthenticatedUser();
        ServiceOrder so = findOsAndValidateIfNull(id);
        validateOsAcessByWorkspace(user, so);
        validateOsAcessByUserOpenOs(user, so);

        logger.atInfo().log("Usuário " +
                "{} acessou os detalhes da ordem de serviço {} ",user.getIdentification(),  so.getSoCode());

        return ServiceOrderDetailsDTO.fromEntity(so);
    }

    @Transactional
    public byte[] generateSoReport (Long soId){
        User user = userService.getCurrentAuthenticatedUser();
        ServiceOrder so = findOsAndValidateIfNull(soId);
        validateOsAcessByWorkspace(user, so);
        validateOsAcessByUserOpenOs(user, so);
        validateOsIsReportable(so);
        return pdfReportGenerator.generateServiceOrderReport(so);
    }


    @Transactional
    public void assignOs(Long soId) {
        User user = userService.getCurrentAuthenticatedUser();
        ServiceOrder so = findOsAndValidateIfNull(soId);
        validateOsAcessByWorkspace(user, so);
        validateIfOsIsAssignable(user, so);
        SoState assignState = new  SoState(null, LocalDateTime.now(), SoStateType.ASSIGNED, so);
        so.getStates().add(assignState);
        so.setCurrentState(SoStateType.ASSIGNED);
        so.setAssignee(user);

        serviceOrderRepository.save(so);
        soStateRepository.save(assignState);
    }

    private void validateIfOsIsAssignable(User user, ServiceOrder so) {
        if (!List.of(Role.ADMIN, Role.SUPPORTER).contains(user.getRole())) {
            throw new BusinessRuleException("Para se responsabilizar por uma tarefa você deve ser SUPPORTER ou ADMIN, seu cargo é de "+user.getRole().toString());
        } else if ( !so.getCurrentState().equals(SoStateType.OPEN)){
            throw new BusinessRuleException("Para se responsablizar por uma ordem de serviço ela deve estar aberta, atualmente ela está "+so.getCurrentState().getName());
        }
    }

    private void validateOsIsReportable(ServiceOrder so) {
        if (!List.of(SoStateType.CANCELED, SoStateType.COMPLETED).contains(so.getCurrentState())){
            throw new BusinessRuleException("Não é possível gerar um relatório de uma ordem de serviço antes dela estar completa ou cancelada!");
        }
    }

    // busca os no banco e valida se existe
    private ServiceOrder findOsAndValidateIfNull(Long soId) {
        ServiceOrder so = serviceOrderRepository.findById(soId).orElseThrow(() -> new ResourceNotFoundException("Ordem de serviço com id: " + soId + " não foi encontrado!"));
        return so;
    }

    // valida com base nos cargos se o usuário pode acessar uma Os que ele não abriu
    private static void validateOsAcessByUserOpenOs(User user, ServiceOrder so) {
        if (user.getRole().equals(Role.COLLABORATOR) && !user.getOpenSo().contains(so)) {
            throw new BusinessRuleException("Como Collaborator você não tem acesso a outras ordens de serviço além das suas abertas");
        }
    }

    // valida se a os está no mesmo workspace que o usuário
    private static void validateOsAcessByWorkspace(User user, ServiceOrder so) {
        Workspace workspace = user.getWorkspace();
        if (!so.getWorkspace().getId().equals(workspace.getId())) {
            throw new BusinessRuleException("Você não tem acesso a ordens de serviço fora do seu workspace");
        }
    }

    public String generateSoCode() {
        String codigo;
        int tentativas = 0;
        final int LIMITE_TENTATIVAS = 100;

        do {
            int numero = random.nextInt(100_000); // Gera número entre 0 e 99999
            codigo = String.format("CH-%05d", numero);
            tentativas++;
            if (tentativas > LIMITE_TENTATIVAS) {
                throw new RuntimeException("Não foi possível gerar um código único de OS após várias tentativas.");
            }
        } while (serviceOrderRepository.existsBySoCodeAndWorkspace_Id(codigo, userService.getCurrentAuthenticatedUser().getWorkspace().getId()));

        return codigo;
    }

}
