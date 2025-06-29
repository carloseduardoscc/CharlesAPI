package com.carlos.charles_api.controller;

import com.carlos.charles_api.dto.request.AuthenticationRequestDTO;
import com.carlos.charles_api.dto.response.LoginResponseDTO;
import com.carlos.charles_api.dto.request.RegisterRequestDTO;
import com.carlos.charles_api.dto.response.UserInfoDTO;
import com.carlos.charles_api.model.entity.User;
import com.carlos.charles_api.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService service;

    //faz login e entrega o JWT
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationRequestDTO data) {
        LoginResponseDTO response = service.login(data);
        return ResponseEntity.ok(response);
    }

    //cria um usuário owner com seu workspace
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterRequestDTO registerDTO) {
        service.register(registerDTO);
        LoginResponseDTO login = service.login(new AuthenticationRequestDTO(registerDTO.email(), registerDTO.password()));
        return ResponseEntity.ok(login);
    }

    //informa dados do usuário autenticado
    @GetMapping("/me")
    public ResponseEntity<UserInfoDTO> me() {
        UserInfoDTO userInfo = service.me();
        return ResponseEntity.ok(userInfo);
    }


}
