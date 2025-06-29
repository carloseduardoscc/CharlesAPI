package com.carlos.charles_api.dto.response;

import com.carlos.charles_api.model.entity.User;

public record UserInfoDTO(
        Long id,
        String email,
        String name,
        String lastname,
        String role,
        Long workspaceId,
        String workspaceIdentification
) {
    public static UserInfoDTO fromEntity(User user){
        return new UserInfoDTO(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getLastName(),
                user.getRole().name(),
                user.getWorkspace().getId(),
                user.getWorkspace().getIdentification()
        );

    }
}
