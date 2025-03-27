package com.carlos.charles_api.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public record EmailDTO(@NotBlank String owner, @Email @NotBlank String from, @Email @NotBlank String to, @NotBlank String subject, @NotBlank String body) {
}
