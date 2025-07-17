package com.carlos.charles_api.repository;

import com.carlos.charles_api.model.entity.ServiceOrder;
import com.carlos.charles_api.model.entity.Workspace;
import com.carlos.charles_api.model.enums.SoStateType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.List;

public interface ServiceOrderRepository extends JpaRepository<ServiceOrder, Long>, JpaSpecificationExecutor<ServiceOrder> {
    boolean existsBySoCodeAndWorkspace_Id(String soCode, Long workspaceId);
}