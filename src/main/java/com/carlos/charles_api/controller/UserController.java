package com.carlos.charles_api.controller;

import com.carlos.charles_api.dto.request.AuthenticationRequestDTO;
import com.carlos.charles_api.dto.request.NewParticipantDTO;
import com.carlos.charles_api.dto.request.RegisterRequestDTO;
import com.carlos.charles_api.dto.response.LoginResponseDTO;
import com.carlos.charles_api.dto.response.NewParticipantResponseDTO;
import com.carlos.charles_api.dto.response.UserInfoDTO;
import com.carlos.charles_api.service.AuthenticationService;
import com.carlos.charles_api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/participants")
public class UserController {

    @Autowired
    UserService userService;

    //cria um usuário owner com seu workspace
    @PostMapping
    public ResponseEntity newParticipant(@RequestBody @Valid NewParticipantDTO  newParticipantDTO) {
        NewParticipantResponseDTO response = userService.addParticipant(newParticipantDTO);
        return ResponseEntity.ok(response);
    }


}
