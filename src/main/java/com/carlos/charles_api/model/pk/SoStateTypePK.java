package com.carlos.charles_api.model.pk;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class SoStateTypePK {
    private com.carlos.charles_api.model.SoStateType type;
    private LocalDateTime dateTime;
}
