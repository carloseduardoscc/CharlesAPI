package com.carlos.charles_api.repository;

import com.carlos.charles_api.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Long> {
    // Metodo customizado para buscar pelo e-mail
    UserDetails findByEmail(String email);
}
