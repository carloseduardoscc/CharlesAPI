package com.carlos.charles_api.service;

import com.carlos.charles_api.model.*;
import com.carlos.charles_api.model.dto.OpenServiceOrderRequestDTO;
import com.carlos.charles_api.model.enums.FaceRole;
import com.carlos.charles_api.model.enums.SoStateType;
import com.carlos.charles_api.repository.FaceRepository;
import com.carlos.charles_api.repository.ServiceOrderRepository;
import com.carlos.charles_api.repository.WorkspaceRepository;
import com.carlos.charles_api.service.exceptions.BusinessRuleException;
import com.carlos.charles_api.service.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.Random;

@Service
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

    public ServiceOrder create(OpenServiceOrderRequestDTO soData, Long workspaceId) {
        // Busca de dados
        User user = uService.getCurrentAuthenticatedUser();
        Workspace workspace = wRepo.findById(workspaceId).orElseThrow(() -> new ResourceNotFoundException("Workspace com ID " + workspaceId + " não foi encontrado!"));
        Face face = fService.getFaceByWorkspaceAndUser(workspace, user);

        // Filtros de regra de negócio (proteção extra!)
        if (face.getRole() != FaceRole.COLLABORATOR) {
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

        logger.atInfo().log("\nNova ordem de serviço:\n"+so.getDescription()+"\nUser: "+user.getEmail()+"\nWorkspace: "+workspace.getIdentification());

        return so;
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
