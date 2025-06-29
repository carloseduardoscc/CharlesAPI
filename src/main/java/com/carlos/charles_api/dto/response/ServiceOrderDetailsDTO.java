package com.carlos.charles_api.dto.response;

import com.carlos.charles_api.model.entity.ServiceOrder;
import com.carlos.charles_api.model.enums.SoStateType;

import java.util.List;
import java.util.stream.Collectors;

public record ServiceOrderDetailsDTO(
        Long id,
        String soCode,
        String description,
        String diagnostic,
        UserInfoDTO assignee,
        UserInfoDTO solicitant,
        List<SoStateResponseDTO> states,
        String currentState
) {
    public static ServiceOrderDetailsDTO fromEntity(ServiceOrder serviceOrder) {
        return new ServiceOrderDetailsDTO(
                serviceOrder.getId(),
                serviceOrder.getSoCode(),
                serviceOrder.getDescription(),
                serviceOrder.getDiagnostic(),
                serviceOrder.getAssignee() != null ? UserInfoDTO.fromEntity(serviceOrder.getAssignee()) : null,
                UserInfoDTO.fromEntity(serviceOrder.getSolicitant()),
                serviceOrder.getStates()
                        .stream()
                        .map(SoStateResponseDTO::fromEntity)
                        .toList(),
                serviceOrder.getCurrentState().name()
        );
    }
}