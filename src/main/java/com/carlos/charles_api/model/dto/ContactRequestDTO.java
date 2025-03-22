package com.carlos.charles_api.model.dto;

import com.carlos.charles_api.model.enums.PersonType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ContactRequestDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String phone;
    @Email
    @NotBlank
    private String email;
    private String city;
    private PersonType personType;
    @NotBlank
    private String message;
}
