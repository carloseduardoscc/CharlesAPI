package com.carlos.charles_api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AuthenticationRequestDTO(
        @NotBlank(message = "O email é obrigatório")
        String login,

        @NotBlank(message = "A senha é obrigatória")
        String password
) {
}
