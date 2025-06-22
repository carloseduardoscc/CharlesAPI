package com.carlos.charles_api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterDTO(@NotBlank @Email String email,
                          @NotBlank @Size(min= 8) String password,
                          @NotBlank @Size(max = 50) String name,
                          @NotBlank @Size(max = 50) String lastName) {
}
