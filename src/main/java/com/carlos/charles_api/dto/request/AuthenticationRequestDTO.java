package com.carlos.charles_api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AuthenticationRequestDTO(
        @NotNull
        @NotBlank(message = "O email é obrigatório")
        @Email(message = "O campo de email está inválido")
        String login,

        @NotNull
        @NotBlank(message = "A senha é obrigatória")
        @Size(min=6, max=50)
        String password
) {
}
