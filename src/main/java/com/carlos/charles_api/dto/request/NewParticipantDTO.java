package com.carlos.charles_api.dto.request;

import com.carlos.charles_api.model.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NewParticipantDTO(
        @NotBlank
        @Size(max = 50)
        String name,
        @NotBlank
        @Size(max = 50)
        String lastName,
        @NotBlank
        @Email
        String email,
        @Size(min=6, max=50)
        @NotBlank
        String password,
        @NotNull
        Role role
) {
}
