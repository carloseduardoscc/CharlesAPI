package com.carlos.charles_api.service;

import com.carlos.charles_api.dto.request.OpenServiceOrderRequestDTO;
import com.carlos.charles_api.dto.response.ServiceOrderSummaryDTO;
import com.carlos.charles_api.exceptions.BusinessRuleException;
import com.carlos.charles_api.model.entity.*;
import com.carlos.charles_api.model.enums.Role;
import com.carlos.charles_api.repository.ServiceOrderRepository;
import com.carlos.charles_api.repository.SoStateRepository;
import com.carlos.charles_api.repository.WorkspaceRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class ServiceOrderService {

    @Autowired
    private ServiceOrderRepository serviceOrderRepository;

    @Autowired
    private UserService userService;

    private Random random = new Random();

    private static final Logger logger = LoggerFactory.getLogger(ServiceOrderService.class);

    @Transactional
    public Long openNewServiceOrder(OpenServiceOrderRequestDTO soData) {
        User user = userService.getCurrentAuthenticatedUser();
        Workspace workspace = user.getWorkspace();

        if (!List.of(Role.COLLABORATOR, Role.OWNER).contains(user.getRole())) {
            throw new BusinessRuleException("Apenas Collaborators e Owners podem abrir ordem de serviço! O usuário atual tem permissões de " + user.getRole().toString());
        }

        ServiceOrder so = new ServiceOrder(generateSoCode(), soData.description(), workspace, user);
        serviceOrderRepository.save(so);

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
