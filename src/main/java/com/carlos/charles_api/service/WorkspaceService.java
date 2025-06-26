package com.carlos.charles_api.service;

import com.carlos.charles_api.dto.response.WorkspaceResponseDTO;
import com.carlos.charles_api.model.entity.User;
import com.carlos.charles_api.model.entity.Workspace;
import com.carlos.charles_api.model.enums.UserRole;
import com.carlos.charles_api.repository.UserRepository;
import com.carlos.charles_api.repository.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
//todo não tem user intermedia, é user direto
public class WorkspaceService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public Workspace createWorkspace(String identification) {
        // Get the current authenticated user
        User currentUser = userService.getCurrentAuthenticatedUser();

        // Create and save the new workspace
        Workspace workspace = new Workspace();
        workspace.setIdentification(identification);
        workspace = workspaceRepository.save(workspace);

        // Create a user with owner role for the current user
        currentUser.setWorkspace(workspace);
        currentUser.setRole(UserRole.OWNER);
        userRepository.save(ownerUser);

        // Return the workspace as a DTO
        return workspace;
    }
}
