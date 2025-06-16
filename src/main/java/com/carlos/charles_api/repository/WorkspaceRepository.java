package com.carlos.charles_api.repository;

import com.carlos.charles_api.model.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {
}
