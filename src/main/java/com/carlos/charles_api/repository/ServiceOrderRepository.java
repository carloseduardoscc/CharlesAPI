package com.carlos.charles_api.repository;

import com.carlos.charles_api.model.entity.ServiceOrder;
import com.carlos.charles_api.model.entity.Workspace;
import com.carlos.charles_api.model.enums.SoStateType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.List;

public interface ServiceOrderRepository extends JpaRepository<ServiceOrder, Long>, JpaSpecificationExecutor<ServiceOrder> {
    boolean existsBySoCode(String codigo);

    List<ServiceOrder> findByWorkspaceId(Long workspaceId);

    List<ServiceOrder> findByWorkspaceIdAndSolicitantId(Long workspaceId, Long collaboratorId);

    boolean existsBySoCodeAndWorkspace_Id(String soCode, Long workspaceId);

    Integer countByWorkspaceAndCurrentStateIsIn(Workspace workspace, Collection<SoStateType> currentStates);

    Integer countByWorkspace(Workspace workspace);
}