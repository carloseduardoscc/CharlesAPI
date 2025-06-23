package com.carlos.charles_api.service;

import com.carlos.charles_api.dto.request.WorkspaceRequestDTO;
import com.carlos.charles_api.dto.response.WorkspaceResponseDTO;
import com.carlos.charles_api.model.entity.Face;
import com.carlos.charles_api.model.entity.User;
import com.carlos.charles_api.model.entity.Workspace;
import com.carlos.charles_api.model.enums.EntityState;
import com.carlos.charles_api.model.enums.FaceRole;
import com.carlos.charles_api.repository.FaceRepository;
import com.carlos.charles_api.repository.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
//todo não tem face intermedia, é user direto
public class WorkspaceService {

    @Autowired
    private FaceRepository faceRepository;

    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Autowired
    private UserService userService;

    public List<WorkspaceResponseDTO> listWorkspacesForCurrentUser() {
        User currentUser = userService.getCurrentAuthenticatedUser();
        List<Face> userFaces = faceRepository.findByUserId(currentUser.getId());

        // Extract workspaces from faces, convert to DTOs, and return as a list
        return userFaces.stream()
                .map(Face::getWorkspace)
                .map(WorkspaceResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public WorkspaceResponseDTO createWorkspace(WorkspaceRequestDTO workspaceRequestDTO) {
        // Get the current authenticated user
        User currentUser = userService.getCurrentAuthenticatedUser();

        // Create and save the new workspace
        Workspace workspace = new Workspace();
        workspace.setIdentification(workspaceRequestDTO.getIdentification());
        workspace = workspaceRepository.save(workspace);

        // Create a face with owner role for the current user
        Face ownerFace = new Face();
        ownerFace.setUser(currentUser);
        ownerFace.setWorkspace(workspace);
        ownerFace.setRole(FaceRole.OWNER);
        ownerFace.setState(EntityState.ACTIVE);
        faceRepository.save(ownerFace);

        // Return the workspace as a DTO
        return WorkspaceResponseDTO.fromEntity(workspace);
    }
}
