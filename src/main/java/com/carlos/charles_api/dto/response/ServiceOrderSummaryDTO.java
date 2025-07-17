package com.carlos.charles_api.dto.response;

import com.carlos.charles_api.model.entity.ServiceOrder;
import com.carlos.charles_api.model.enums.SoStateType;

public record ServiceOrderSummaryDTO(
        Long id,
        String soCode,
        String description,
        SoStateType currentState,
        String collaboratorName,
        String collaboratorEmail,
        String supporterName,
        String supporterEmail
) {
    public static ServiceOrderSummaryDTO fromEntity(ServiceOrder serviceOrder) {
        return new ServiceOrderSummaryDTO(
                serviceOrder.getId(),
                serviceOrder.getSoCode(),
                serviceOrder.getDescription(),
                serviceOrder.getCurrentState(),
                serviceOrder.getSolicitant() != null ? serviceOrder.getSolicitant().getFullName() : null,
                serviceOrder.getSolicitant() != null ? serviceOrder.getSolicitant().getEmail() : null,
                serviceOrder.getAssignee() != null ? serviceOrder.getAssignee().getFullName() : null,
                serviceOrder.getAssignee() != null ? serviceOrder.getAssignee().getEmail() : null
                );
    }
}