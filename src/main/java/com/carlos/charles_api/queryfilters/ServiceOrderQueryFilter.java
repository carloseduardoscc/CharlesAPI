package com.carlos.charles_api.queryfilters;

import com.carlos.charles_api.model.entity.ServiceOrder;
import com.carlos.charles_api.model.enums.SoStateType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.List;

import static com.carlos.charles_api.model.specifications.ServiceOrderSpec.*;

@Getter
@Setter
public class ServiceOrderQueryFilter {
    private String assignee;
    private LocalDate minDate;
    private LocalDate maxDate;
    private SoStateType state;

    public Specification<ServiceOrder> toSpecification() {
        return hasAssigneeName(assignee)
                .and(hasDateBetween(minDate, maxDate)
                        .and(hasCurrentStatus(ObjectUtils.isEmpty(state) ? null : List.of(state)))
                );
    }
}
