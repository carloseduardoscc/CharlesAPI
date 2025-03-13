package com.carlos.charles_api.model;

import com.carlos.charles_api.model.enums.SoStateType;
import com.carlos.charles_api.model.pk.SoStatePK;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Entity(name = "soState_tb")
//Possível problema com esse novo @IdClass
@IdClass(SoStatePK.class)
public class SoState {
    @Id
    @Enumerated(EnumType.STRING)
    private SoStateType type;
    @Id
    private LocalDateTime dateTime;
}
