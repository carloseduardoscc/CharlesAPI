package com.carlos.charles_api.dto.response;

import com.carlos.charles_api.model.entity.ServiceOrder;
import com.carlos.charles_api.model.enums.SoStateType;

public record ServiceOrderResponseDTO(
    Long id,
    String soCode,
    String description,
    String diagnostic,
    SoStateType currentState,
    Long workspaceId,
    String workspaceIdentification,
    Long collaboratorId,
    String collaboratorName,
    Long supporterId,
    String supporterName
) {
    public static ServiceOrderResponseDTO fromEntity(ServiceOrder serviceOrder) {
        return new ServiceOrderResponseDTO(
            serviceOrder.getId(),
            serviceOrder.getSoCode(),
            serviceOrder.getDescription(),
            serviceOrder.getDiagnostic(),
            serviceOrder.getCurrentState(),
            serviceOrder.getWorkspace().getId(),
            serviceOrder.getWorkspace().getIdentification(),
            serviceOrder.getCollaborator() != null ? serviceOrder.getCollaborator().getId() : null,
            serviceOrder.getCollaborator() != null ? serviceOrder.getCollaborator().getUser().getFullName() : null,
            serviceOrder.getSupporter() != null ? serviceOrder.getSupporter().getId() : null,
            serviceOrder.getSupporter() != null ? serviceOrder.getSupporter().getUser().getFullName() : null
        );
    }
}