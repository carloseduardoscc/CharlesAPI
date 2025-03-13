package com.carlos.charles_api.model.pk;

import com.carlos.charles_api.model.SoState;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class SoStatePK {
    private SoState type;
    private LocalDateTime dateTime;
}
