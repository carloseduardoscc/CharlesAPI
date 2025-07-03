package com.carlos.charles_api.repository;

import com.carlos.charles_api.model.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Long> {
    // Metodo customizado para buscar pelo e-mail
    User findByEmail(String email);

    boolean existsByEmail(@NotBlank @Email String email);
}
