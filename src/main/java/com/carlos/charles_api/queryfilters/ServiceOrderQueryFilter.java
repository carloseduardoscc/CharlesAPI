package com.carlos.charles_api.queryfilters;

import com.carlos.charles_api.model.entity.ServiceOrder;
import com.carlos.charles_api.model.enums.SoStateType;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

import static com.carlos.charles_api.model.specifications.ServiceOrderSpec.*;

@Data
public class ServiceOrderQueryFilter {
    private String assignee;
    private LocalDate minDate;
    private LocalDate maxDate;
    private SoStateType state;

    public Specification<ServiceOrder> toSpecification() {
        return hasAssigneeName(assignee)
                .and(hasDateBetween(minDate, maxDate)
                        .and(hasCurrentStatus(state))
                );
    }
}
