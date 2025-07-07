package com.carlos.charles_api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record OsDiagnosticDTO(
        @NotBlank(message = "O diagnóstico é obrigatório para concluir a ordem de serviço ")
        String diagnostic
) {}