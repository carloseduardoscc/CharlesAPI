package com.carlos.charles_api.model.dto;

import com.carlos.charles_api.model.enums.PersonType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public record ContactRequestDTO(@NotBlank String name, @NotBlank String phone, @Email @NotBlank String email, String city, PersonType personType, @NotBlank String message) {
}
