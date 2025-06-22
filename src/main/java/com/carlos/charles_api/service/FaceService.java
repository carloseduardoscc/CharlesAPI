package com.carlos.charles_api.service;

import com.carlos.charles_api.exceptions.ResourceNotFoundException;
import com.carlos.charles_api.model.entity.Face;
import com.carlos.charles_api.model.entity.User;
import com.carlos.charles_api.model.entity.Workspace;
import org.springframework.stereotype.Service;

@Service
public class FaceService {

    public Face getFaceByWorkspaceAndUser(Workspace workspace, User user) {
        return workspace.getFaces().stream()
                .filter(f -> f.getUser().getId().equals(user.getId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Usuário " + user.getEmail() + " não pertence ao workspace " + workspace.getIdentification() + " ID: " + workspace.getId()));
    }

}
