package com.carlos.charles_api.queryfilters;

import com.carlos.charles_api.model.entity.ServiceOrder;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

import static com.carlos.charles_api.model.specifications.ServiceOrderSpec.*;

@Data
public class MinDateMaxDateFilter {
    private LocalDate minDate;
    private LocalDate maxDate;

    public Specification<ServiceOrder> toSpecification() {
        return hasDateBetween(minDate, maxDate);
    }
}
