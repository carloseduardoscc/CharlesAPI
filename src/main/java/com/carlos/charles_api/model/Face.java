package com.carlos.charles_api.model;

import com.carlos.charles_api.model.enums.EntityState;
import com.carlos.charles_api.model.enums.FaceRole;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Face {
    private Long id;

    //External
    private EntityState state;
    private List<ServiceOrder> managedSO;
    private FaceRole role;
    private Workspace workspace;
}
