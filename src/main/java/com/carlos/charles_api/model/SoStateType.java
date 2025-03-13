package com.carlos.charles_api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
//todo Pode dar ruim, é coisa nova
public class SoStateType {
    private SoStateType type;
    private LocalDateTime dateTime;
}
