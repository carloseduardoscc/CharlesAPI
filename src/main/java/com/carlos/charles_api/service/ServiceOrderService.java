package com.carlos.charles_api.service;

import com.carlos.charles_api.dto.request.OpenServiceOrderDTO;
import com.carlos.charles_api.dto.response.ServiceOrderResponseDTO;
import com.carlos.charles_api.exceptions.BusinessRuleException;
import com.carlos.charles_api.exceptions.ResourceNotFoundException;
import com.carlos.charles_api.model.entity.*;
import com.carlos.charles_api.model.enums.UserRole;
import com.carlos.charles_api.model.enums.SoStateType;
import com.carlos.charles_api.repository.ServiceOrderRepository;
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
//todo não tem face intermedia, é user direto
public class ServiceOrderService {

    @Autowired
    private ServiceOrderRepository sRepo;

    @Autowired
    private WorkspaceRepository wRepo;

    @Autowired
    private FaceService fService;

    @Autowired
    private UserService uService;

    @Autowired
    private FaceRepository fRepo;

    private static final Logger logger = LoggerFactory.getLogger(ServiceOrderService.class);

    private Random random = new Random();

    @Transactional
    public ServiceOrder create(OpenServiceOrderDTO soData, Long workspaceId) {
        // Busca de dados
        User user = uService.getCurrentAuthenticatedUser();
        Workspace workspace = wRepo.findById(workspaceId).orElseThrow(() -> new ResourceNotFoundException("Workspace com ID " + workspaceId + " não foi encontrado!"));
        Face face = fService.getFaceByWorkspaceAndUser(workspace, user);

        // Filtros de regra de negócio (proteção extra!)
        if (face.getRole() != UserRole.COLLABORATOR) {
            throw new BusinessRuleException("Apenas Colaboradores 'COLLABORATOR' podem abrir ordem de serviço! O usuário atual tem permissões de " + face.getRole().toString());
        }

        // Montagem da entidade
        ServiceOrder so = new ServiceOrder();
        so.setSoCode(generateSoCode());
        so.setDescription(soData.description());
        so.setWorkspace(workspace);
        so.setCollaborator(face);
        so.getStates().add(new SoState(null, LocalDateTime.now(), SoStateType.OPEN, so));
        so.setCurrentState(SoStateType.OPEN);

        // Atualização das entidades relacionadas
        workspace.getServiceOrders().add(so);
        face.getOpenSO().add(so);

        // persistindo entidades modificadas
        sRepo.save(so);

        logger.atInfo().log("\nNova ordem de serviço:\n" + so.getDescription() + "\nUser: " + user.getEmail() + "\nWorkspace: " + workspace.getIdentification());

        return so;
    }

    @Transactional
    public List<ServiceOrderResponseDTO> list(Long workspaceId) {
        User user = uService.getCurrentAuthenticatedUser();

        Workspace workspace = wRepo.findById(workspaceId).get();

        // Get the user's face for this workspace to determine their role
        Face face = fService.getFaceByWorkspaceAndUser(workspace, user);

        List<ServiceOrder> serviceOrders;

        // Apply access control based on user role
        if (face.getRole() == UserRole.COLLABORATOR) {
            // Collaborators can only see service orders they opened
            serviceOrders = sRepo.findByWorkspaceIdAndCollaboratorId(workspaceId, face.getId());
            logger.atInfo().log("User {} with COLLABORATOR role accessing their service orders in workspace {}",
                    user.getEmail(), workspace.getIdentification());
        } else if (face.getRole() == UserRole.SUPPORTER ||
                face.getRole() == UserRole.ADMIN ||
                face.getRole() == UserRole.OWNER) {
            // Supporters, Admins, and Owners can see all service orders in the workspace
            serviceOrders = sRepo.findByWorkspaceId(workspaceId);
            logger.atInfo().log("User {} with {} role accessing all service orders in workspace {}",
                    user.getEmail(), face.getRole(), workspace.getIdentification());
        } else {
            // This should never happen, but just in case
            throw new BusinessRuleException("Usuário com papel desconhecido: " + face.getRole());
        }

        // Convert entities to DTOs
        return serviceOrders.stream()
                .map(ServiceOrderResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public String generateSoCode() {
        String codigo;
        int tentativas = 0;
        final int LIMITE_TENTATIVAS = 100;

        do {
            int numero = random.nextInt(100_000); // Gera número entre 0 e 99999
            codigo = String.format("SO-%05d", numero);
            tentativas++;
            if (tentativas > LIMITE_TENTATIVAS) {
                throw new RuntimeException("Não foi possível gerar um código único de OS após várias tentativas.");
            }
        } while (sRepo.existsBySoCode(codigo));

        return codigo;
    }

}
