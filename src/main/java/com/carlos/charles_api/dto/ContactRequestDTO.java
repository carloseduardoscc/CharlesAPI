package com.carlos.charles_api.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
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
    private String personType;
    @NotBlank
    private String message;
}
