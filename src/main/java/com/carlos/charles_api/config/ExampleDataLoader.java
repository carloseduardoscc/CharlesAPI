package com.carlos.charles_api.config;

import com.carlos.charles_api.model.entity.ServiceOrder;
import com.carlos.charles_api.model.entity.SoState;
import com.carlos.charles_api.model.entity.User;
import com.carlos.charles_api.model.entity.Workspace;
import com.carlos.charles_api.model.enums.SoStateType;
import com.carlos.charles_api.model.enums.Role;
import com.carlos.charles_api.repository.*;
import com.carlos.charles_api.service.ServiceOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ExampleDataLoader implements CommandLineRunner
{

    @Autowired
    UserRepository userRepo;
    @Autowired
    WorkspaceRepository workspaceRepo;
    @Autowired
    ServiceOrderRepository soRepo;
    @Autowired
    SoStateRepository soStateRepo;
    @Autowired
    PasswordEncoder encoder;

    private static final Logger logger = LoggerFactory.getLogger(ServiceOrderService.class);

    @Override
    public void run(String... args) throws Exception {

        logger.atInfo().log("Carregando dados de exemplo...");

        // Create workspaces
        Workspace workspace1 = new Workspace("Workspace1");
        Workspace workspace2 = new Workspace("Workspace2");
        Workspace workspace3 = new Workspace("Workspace3");

        workspaceRepo.saveAll(List.of(workspace1, workspace2, workspace3));

        // Create users with different roles
        User owner1 = new User(
                "owner1@example.com",
                encoder.encode("owner123"),
                "Owner1",
                "Name",
                Role.OWNER,
                workspace1
        );

        User owner2 = new User(
                "owner2@example.com",
                encoder.encode("owner123"),
                "Owner2",
                "Name",
                Role.OWNER,
                workspace2
        );

        User admin1 = new User(
                "admin1@example.com",
                encoder.encode("admin123"),
                "Admin1",
                "Name",
                Role.ADMIN,
                workspace1
        );

        User admin2 = new User(
                "admin2@example.com",
                encoder.encode("admin123"),
                "Admin2",
                "Name",
                Role.ADMIN,
                workspace2
        );

        User supporter1 = new User(
                "supporter1@example.com",
                encoder.encode("support123"),
                "Supporter1",
                "Name",
                Role.SUPPORTER,
                workspace1
        );

        User supporter2 = new User(
                "supporter2@example.com",
                encoder.encode("support123"),
                "Supporter2",
                "Name",
                Role.SUPPORTER,
                workspace1
        );

        User supporter3 = new User(
                "supporter3@example.com",
                encoder.encode("support123"),
                "Supporter3",
                "Name",
                Role.SUPPORTER,
                workspace2
        );

        User collaborator1 = new User(
                "collaborator1@example.com",
                encoder.encode("collab123"),
                "Collaborator1",
                "Name",
                Role.COLLABORATOR,
                workspace1
        );

        User collaborator2 = new User(
                "collaborator2@example.com",
                encoder.encode("collab123"),
                "Collaborator2",
                "Name",
                Role.COLLABORATOR,
                workspace1
        );

        User collaborator3 = new User(
                "collaborator3@example.com",
                encoder.encode("collab123"),
                "Collaborator3",
                "Name",
                Role.COLLABORATOR,
                workspace2
        );

        userRepo.saveAll(List.of(owner1, owner2, admin1, admin2, supporter1, supporter2, supporter3, collaborator1, collaborator2, collaborator3));

        // Create service orders
        ServiceOrder so1 = new ServiceOrder();
        so1.setSoCode("CH-00001");
        so1.setDescription("Computador não liga");
        so1.setWorkspace(workspace1);
        so1.setSolicitant(collaborator1);
        so1.setAssignee(supporter1);
        so1.setCurrentState(SoStateType.ASSIGNED);

        ServiceOrder so2 = new ServiceOrder();
        so2.setSoCode("CH-00002");
        so2.setDescription("Precisa instalar um programa");
        so2.setWorkspace(workspace1);
        so2.setSolicitant(collaborator2);
        so2.setAssignee(supporter2);
        so2.setCurrentState(SoStateType.COMPLETED);
        so2.setDiagnostic("programa instalado com sucesso!");

        ServiceOrder so3 = new ServiceOrder();
        so3.setSoCode("CH-00003");
        so3.setDescription("Problema de conexão com internet");
        so3.setWorkspace(workspace1);
        so3.setSolicitant(collaborator1);
        so3.setCurrentState(SoStateType.OPEN);

        ServiceOrder so5 = new ServiceOrder();
        so5.setSoCode("CH-00005");
        so5.setDescription("Interferência na rede");
        so5.setWorkspace(workspace1);
        so5.setAssignee(supporter1);
        so5.setSolicitant(collaborator1);
        so5.setCurrentState(SoStateType.CANCELED);

        ServiceOrder so4 = new ServiceOrder();
        so4.setSoCode("CH-00004");
        so4.setDescription("Impressora não funciona");
        so4.setWorkspace(workspace2);
        so4.setSolicitant(collaborator3);
        so4.setAssignee(supporter2);
        so4.setCurrentState(SoStateType.CANCELED);
        so4.setDiagnostic("Usuário pediu para cancelar");

        soRepo.saveAll(List.of(so1, so2, so3, so5, so4));

        // Create service order states (history)
        // SO1 states
        SoState so1State1 = new SoState(null, LocalDateTime.now().minusDays(2), SoStateType.OPEN, so1);
        SoState so1State2 = new SoState(null, LocalDateTime.now().minusDays(1), SoStateType.ASSIGNED, so1);

        // SO2 states
        SoState so2State1 = new SoState(null, LocalDateTime.now().minusDays(3), SoStateType.OPEN, so2);
        SoState so2State2 = new SoState(null, LocalDateTime.now().minusDays(2), SoStateType.ASSIGNED, so2);
        SoState so2State3 = new SoState(null, LocalDateTime.now().minusDays(1), SoStateType.COMPLETED, so2);

        // SO3 states
        SoState so3State1 = new SoState(null, LocalDateTime.now().minusHours(5), SoStateType.OPEN, so3);

        // SO4 states
        SoState so4State1 = new SoState(null, LocalDateTime.now().minusDays(5), SoStateType.OPEN, so4);
        SoState so4State2 = new SoState(null, LocalDateTime.now().minusDays(4), SoStateType.ASSIGNED, so4);
        SoState so4State3 = new SoState(null, LocalDateTime.now().minusDays(3), SoStateType.CANCELED, so4);

        // SO4 states
        SoState so5State1 = new SoState(null, LocalDateTime.now().minusDays(6), SoStateType.OPEN, so5);
        SoState so5State2 = new SoState(null, LocalDateTime.now().minusDays(5), SoStateType.ASSIGNED, so5);
        SoState so5State3 = new SoState(null, LocalDateTime.now().minusDays(4), SoStateType.CANCELED, so5);

        soStateRepo.saveAll(List.of(
                so1State1, so1State2,
                so2State1, so2State2, so2State3,
                so3State1,
                so4State1, so4State2, so4State3,
                so5State1, so5State2, so5State3
        ));

        logger.atInfo().log("Dados de exemplo carregados com sucesso!");
    }
}
