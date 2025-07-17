package com.carlos.charles_api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(
        @NotBlank
        @Email
        String email,
        @NotBlank
        @Size(min=6, max=50)
        String password,
        @NotBlank
        @Size(max = 50)
        String name,
        @NotBlank
        @Size(max = 50)
        String lastName,
        @NotBlank
        @Size(max = 50)
        String workspaceName) {
}
