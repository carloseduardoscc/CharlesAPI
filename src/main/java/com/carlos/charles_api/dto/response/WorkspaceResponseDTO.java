package com.carlos.charles_api.dto.response;

import com.carlos.charles_api.model.entity.Workspace;

public record WorkspaceResponseDTO(Long id, String identification) {
    
    public static WorkspaceResponseDTO fromEntity(Workspace workspace) {
        return new WorkspaceResponseDTO(
            workspace.getId(),
            workspace.getIdentification()
        );
    }
}