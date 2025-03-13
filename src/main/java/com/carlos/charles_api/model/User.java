package com.carlos.charles_api.model;

import com.carlos.charles_api.model.enums.EntityState;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.autoconfigure.web.WebProperties;

import java.util.List;

@Data
@AllArgsConstructor
public class User {
    private Long id;

    private String userId;
    private String name;
    private String lastName;

    //External
    private EntityState state;
    private List<Face> faces;
}
