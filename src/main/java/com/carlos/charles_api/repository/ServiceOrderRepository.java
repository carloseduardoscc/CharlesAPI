package com.carlos.charles_api.repository;

import com.carlos.charles_api.model.entity.ServiceOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceOrderRepository extends JpaRepository<ServiceOrder, Long> {
}
