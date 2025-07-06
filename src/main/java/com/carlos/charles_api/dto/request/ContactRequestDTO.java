package com.carlos.charles_api.dto.request;

import com.carlos.charles_api.model.enums.PersonType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ContactRequestDTO(

        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 100, message = "O nome não pode ter mais que {max} caracteres")
        String name,

        @NotBlank(message = "O telefone é obrigatório")
        String phone,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "Informe um e-mail válido")
        String email,

        @Size(max = 50, message = "A cidade não pode ter mais que {max} caracteres")
        String city,

        PersonType personType,

        @NotBlank(message = "A mensagem é obrigatória")
        @Size(max = 500, message = "A mensagem não pode exceder {max} caracteres")
        String message

) {}