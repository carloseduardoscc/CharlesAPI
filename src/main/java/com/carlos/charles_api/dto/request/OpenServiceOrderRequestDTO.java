package com.carlos.charles_api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record OpenServiceOrderRequestDTO(
        @NotBlank(message = "A descrição é obrigatória para abrir a ordem de serviço ")
        String description
) {}