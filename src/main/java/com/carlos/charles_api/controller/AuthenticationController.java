package com.carlos.charles_api.controller;

import com.carlos.charles_api.dto.request.AuthenticationDTO;
import com.carlos.charles_api.dto.LoginResponseDTO;
import com.carlos.charles_api.dto.RegisterDTO;
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

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data) {
        LoginResponseDTO response = service.login(data);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO registerDTO) {
        service.register(registerDTO);
        return ResponseEntity.ok("{}");
    }


}
