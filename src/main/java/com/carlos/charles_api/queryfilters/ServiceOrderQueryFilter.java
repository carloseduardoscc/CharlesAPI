package com.carlos.charles_api.queryfilters;

import com.carlos.charles_api.model.entity.ServiceOrder;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

import static com.carlos.charles_api.model.specifications.ServiceOrderSpec.*;

@Data
public class ServiceOrderQueryFilter {
    private String assignee;
    private LocalDate minDate;
    private LocalDate maxDate;

    public Specification<ServiceOrder> toSpecification() {
        return hasAssigneeName(assignee)
                .and(hasDateBetween(minDate, maxDate)
                );
    }
}
