package com.carlos.charles_api.config;

import com.carlos.charles_api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

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

    }
}
