package com.carlos.charles_api.repository;

import com.carlos.charles_api.model.entity.Face;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FaceRepository extends JpaRepository<Face, Long> {
    public List<Face> findByUserId(Long userId);
}
