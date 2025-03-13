package com.carlos.charles_api.repository;

import com.carlos.charles_api.model.Face;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FaceRepository extends JpaRepository<Face, Long> {
}
