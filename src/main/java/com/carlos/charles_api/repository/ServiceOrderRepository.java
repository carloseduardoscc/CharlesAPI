package com.carlos.charles_api.repository;

import com.carlos.charles_api.model.entity.ServiceOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceOrderRepository extends JpaRepository<ServiceOrder, Long> {
    boolean existsBySoCode(String codigo);

    List<ServiceOrder> findByWorkspaceId(Long workspaceId);

    List<ServiceOrder> findByWorkspaceIdAndSolicitantId(Long workspaceId, Long collaboratorId);

    boolean existsBySoCodeAndWorkspace_Id(String soCode, Long workspaceId);

    String findSoCodeById(Long id);
}