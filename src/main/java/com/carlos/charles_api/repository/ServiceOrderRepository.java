package com.carlos.charles_api.repository;

import com.carlos.charles_api.model.entity.ServiceOrder;
import com.carlos.charles_api.model.entity.Workspace;
import com.carlos.charles_api.model.enums.SoStateType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ServiceOrderRepository extends JpaRepository<ServiceOrder, Long> {
    boolean existsBySoCode(String codigo);

    List<ServiceOrder> findByWorkspaceId(Long workspaceId);

    List<ServiceOrder> findByWorkspaceIdAndSolicitantId(Long workspaceId, Long collaboratorId);

    boolean existsBySoCodeAndWorkspace_Id(String soCode, Long workspaceId);

    String findSoCodeById(Long id);

    Integer findByWorkspaceAndSoCode(Workspace workspace, String soCode);

    List<ServiceOrder> findByWorkspaceAndCurrentStateIsIn(Workspace workspace, Collection<SoStateType> currentStates);

    long countByWorkspaceAndCurrentState(Workspace workspace, SoStateType currentState);

    Integer countByWorkspaceAndCurrentStateIsIn(Workspace workspace, Collection<SoStateType> currentStates);

    Integer countByWorkspace(Workspace workspace);
}