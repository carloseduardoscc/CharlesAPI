package com.carlos.charles_api.config;

import com.carlos.charles_api.model.*;
import com.carlos.charles_api.model.enums.EntityState;
import com.carlos.charles_api.model.enums.FaceRole;
import com.carlos.charles_api.model.enums.SoStateType;
import com.carlos.charles_api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
public class ExampleDataLoader implements CommandLineRunner
{

    @Autowired
    UserRepository userRepo;
    @Autowired
    WorkspaceRepository workspaceRepo;
    @Autowired
    FaceRepository faceRepo;
    @Autowired
    ServiceOrderRepository soRepo;
    @Autowired
    SoStateRepository soStateRepo;
    @Autowired
    PasswordEncoder encoder;

    @Override
    public void run(String... args) throws Exception {
        //Aviso: Regras de negócio não funcionam aqui porque não passa pela camada do service, isso é apenas para população teste de dados!

        // Criando usuários
        User u1 = userRepo.save(new User("joaobezerra@gmail.com",encoder.encode("12345"), "João", "Bezerra"));
        User u2 = userRepo.save(new User("carlosalberto@gmail.com",encoder.encode("abcde"), "Carlos", "Alberto"));
        User u3 = userRepo.save(new User("theodorotorres@gmail.com",encoder.encode("a1b2c3"), "Theodoro", "Torres"));
        User u4 = userRepo.save(new User("leonardoguimaraes@gmail.com",encoder.encode("f1g2h3"), "Leonardo", "Guimaraes"));

        // Usuário u3 desativa sua conta
        u3.setState(EntityState.DISABLED);
        userRepo.save(u3);

        // Usuário u1 criando um workspace e se torna Admin dele
        Workspace w1 = workspaceRepo.save(new Workspace(null, "NewWorkspace"));
        Face f1 = faceRepo.save(new Face(null, u1, FaceRole.ADMIN, w1));
        u1.getFaces().add(f1);
        w1.getFaces().add(f1);

        // Usuário u2 se tornando Collaborator do workspace w1
        Face f2 = faceRepo.save(new Face(null, u2, FaceRole.COLLABORATOR, w1));
        u2.getFaces().add(f2);
        w1.getFaces().add(f2);

        // Order de serviço solicitada pelo f2 com um novo estado de 'CREATED'!
        ServiceOrder so1 = soRepo.save(new ServiceOrder(null, "SO-1A2B3C", "Monitor do computador 27 no setor financeiro não liga", w1, f2));
        f2.getOpenSO().add(so1);
        SoState soState1 = soStateRepo.save(new SoState(null, LocalDateTime.now(), SoStateType.OPEN, so1));
        so1.getStates().add(soState1);

        // f1 se responsabiliza pela so1
        so1.setSupporter(f1);
        f1.getManagedSO().add(so1);
        SoState soState2 = soStateRepo.save(new SoState(null, LocalDateTime.now().plusMinutes(15), SoStateType.ASSIGNED, so1));
        so1.getStates().add(soState2);
        so1.setCurrentState(SoStateType.ASSIGNED);

        // so1 é concluída e é deixado o diagnóstico
        SoState soState3 = soStateRepo.save(new SoState(null, LocalDateTime.now().plusMinutes(45), SoStateType.COMPLETED, so1));
        so1.getStates().add(soState3);
        so1.setCurrentState(SoStateType.COMPLETED);
        so1.setDiagnostic("Monitor com defeito foi substituído");


        //saving last updates
        soRepo.save(so1);
        soStateRepo.saveAll(Arrays.asList(soState1, soState2, soState3));
    }
}
