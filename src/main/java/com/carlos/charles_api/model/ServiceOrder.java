package com.carlos.charles_api.model;

import com.carlos.charles_api.model.enums.SoState;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ServiceOrder {
    private Long id;

    private String soCode;
    private String description;
    private String diagnostic;

    //External
    private Workspace workspace;
    private Face applicant;
    private Face supporter;
    private SoState state;
    private SoStateType currentState;

}
