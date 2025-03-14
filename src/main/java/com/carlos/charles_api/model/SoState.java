package com.carlos.charles_api.model;

import com.carlos.charles_api.model.enums.SoStateType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Entity(name = "soState_tb")
public class SoState {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private SoStateType type;
    private LocalDateTime dateTime;
}
