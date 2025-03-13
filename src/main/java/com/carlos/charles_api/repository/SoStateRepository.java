package com.carlos.charles_api.repository;

import com.carlos.charles_api.model.SoState;
import com.carlos.charles_api.model.pk.SoStatePK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SoStateRepository extends JpaRepository<SoState, SoStatePK> {
}
