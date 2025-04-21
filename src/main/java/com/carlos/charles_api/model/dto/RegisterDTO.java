package com.carlos.charles_api.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterDTO(@NotBlank @Email String email, @NotBlank String password, @NotBlank String name, @NotBlank String lastName) {
}
