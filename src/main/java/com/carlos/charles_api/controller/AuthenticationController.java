package com.carlos.charles_api.controller;

import com.carlos.charles_api.dto.request.AuthenticationRequestDTO;
import com.carlos.charles_api.dto.response.LoginDTO;
import com.carlos.charles_api.dto.request.RegisterRequestDTO;
import com.carlos.charles_api.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService service;

    //faz login e entrega o JWT
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationRequestDTO data) {
        LoginDTO response = service.login(data);
        return ResponseEntity.ok(response);
    }

    //cria um usuário owner com seu workspace
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterRequestDTO registerDTO) {
        service.register(registerDTO);
        return ResponseEntity.ok("{}");
    }


}
