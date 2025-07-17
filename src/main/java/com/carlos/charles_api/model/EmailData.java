package com.carlos.charles_api.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailData(@NotBlank String owner, @Email @NotBlank String from, @Email @NotBlank String to, @NotBlank String subject, @NotBlank String body) {
}
