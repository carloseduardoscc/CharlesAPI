package com.carlos.charles_api.controller;

import com.carlos.charles_api.dto.request.ContactRequestDTO;
import com.carlos.charles_api.service.ContactRequestService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@AllArgsConstructor
@Controller
@RequestMapping("/contactRequest")
public class ContactRequestController {

    private final ContactRequestService service;

    @PostMapping
    public ResponseEntity<ContactRequestDTO> sendEmail(@Valid @RequestBody ContactRequestDTO requestBody){
        service.send(requestBody);
        return ResponseEntity.ok(requestBody);
    }

}
