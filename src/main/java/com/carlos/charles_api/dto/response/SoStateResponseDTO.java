package com.carlos.charles_api.dto.response;

import com.carlos.charles_api.model.entity.SoState;

import java.time.LocalDateTime;

public record SoStateResponseDTO(
        LocalDateTime dateTime,
        String type
) {
    public static SoStateResponseDTO fromEntity(SoState soState) {
        return new SoStateResponseDTO(
                soState.getDateTime(),
                soState.getType().name()
        );
    }
}
