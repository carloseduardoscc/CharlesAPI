package com.carlos.charles_api.service;

import com.carlos.charles_api.model.User;
import com.carlos.charles_api.model.dto.RegisterDTO;
import com.carlos.charles_api.repository.UserRepository;
import com.carlos.charles_api.service.exceptions.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void register(RegisterDTO registerDTO) {
        validateUserRegister(registerDTO);

        String encryptedPassword = passwordEncoder.encode(registerDTO.password());
        User newUser = new User(registerDTO.email(), encryptedPassword, registerDTO.name(), registerDTO.lastName());

        this.repository.save(newUser);
    }

    private void validateUserRegister(RegisterDTO data) {
        if(this.repository.findByEmail(data.email()) != null) throw new UserAlreadyExistsException("O usuário "+ data.email()+" já existe!");
    }

}
