package com.carlos.charles_api.dto.response;

import com.carlos.charles_api.model.entity.User;
import com.carlos.charles_api.model.enums.Role;

public record ParticipantDTO(
        Long id,
        String name,
        String email,
        Role role,
        boolean isActive
) {
    public static ParticipantDTO fromEntity(User user){
        return new ParticipantDTO(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole(),
                user.isEnabled()
        );

    }
}
