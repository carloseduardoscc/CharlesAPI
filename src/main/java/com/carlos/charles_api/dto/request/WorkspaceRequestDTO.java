package com.carlos.charles_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceRequestDTO {
    
    @NotBlank(message = "Workspace identification cannot be blank")
    private String identification;
}