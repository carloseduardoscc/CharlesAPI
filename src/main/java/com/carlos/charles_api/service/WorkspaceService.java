package com.carlos.charles_api.service;

import com.carlos.charles_api.dto.response.WorkspaceResponseDTO;
import com.carlos.charles_api.model.entity.Face;
import com.carlos.charles_api.model.entity.User;
import com.carlos.charles_api.model.entity.Workspace;
import com.carlos.charles_api.repository.FaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkspaceService {

    @Autowired
    private FaceRepository faceRepository;

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
}
