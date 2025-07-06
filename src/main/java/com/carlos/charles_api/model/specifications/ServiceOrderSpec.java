package com.carlos.charles_api.model.specifications;

import com.carlos.charles_api.model.entity.ServiceOrder;
import com.carlos.charles_api.model.entity.SoState;
import com.carlos.charles_api.model.enums.SoStateType;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.time.LocalTime;

public class ServiceOrderSpec {
    public static Specification<ServiceOrder> hasAssigneeName(String assigneeName) {
        return (root, query, cb) -> {
            if (ObjectUtils.isEmpty(assigneeName)) {
                return null;
            }
            return cb.like(
                    cb.lower(
                            cb.concat(
                                    root.get("assignee").get("name"),
                                    root.get("assignee").get("lastName"))),
                    "%" + assigneeName + "%");
        };
    }

    public static Specification<ServiceOrder> hasDateBetween(LocalDate minDate, LocalDate maxDate) {
        return (root, query, cb) -> {
            if (ObjectUtils.isEmpty(minDate) && ObjectUtils.isEmpty(maxDate)) {
                return null;
            }

            Join<ServiceOrder, SoState> states = root.join("states");
            return cb.and(
                    cb.equal(states.get("type"), SoStateType.OPEN),
                    ObjectUtils.isEmpty(minDate) ? cb.conjunction() : cb.greaterThanOrEqualTo(states.get("dateTime"), minDate.atStartOfDay()),
                    ObjectUtils.isEmpty(maxDate) ? cb.conjunction() : cb.lessThanOrEqualTo(states.get("dateTime"), maxDate.atTime(LocalTime.MAX))
            );
        };
    }


    public static Specification<ServiceOrder> hasWorkspaceId(Long workspaceId) {
        return (root, query, cb) -> {
            if (ObjectUtils.isEmpty(workspaceId)) {
                return null;
            }
            return cb.equal(root.get("workspace").get("id"), workspaceId);
        };
    }

    public static Specification<ServiceOrder> hasSolicitantId(Long solicitantId) {
        return (root, query, cb) -> {
            if (ObjectUtils.isEmpty(solicitantId)) {
                return null;
            }
            return cb.equal(root.get("solicitant").get("id"), solicitantId);
        };
    }
}
