package com.carlos.charles_api.model;

import com.carlos.charles_api.model.enums.SoStateType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "soState_tb")
public class SoState {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime dateTime;

    //External
    @Enumerated(EnumType.STRING)
    private SoStateType type;
    @ManyToOne
    @JoinColumn(name = "serviceOrder_id")
    private ServiceOrder serviceOrder;
}
