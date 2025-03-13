package com.carlos.charles_api.repository;

import com.carlos.charles_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Metodo customizado para buscar pelo userId
    Optional<User> findByUserId(String userId);
}
