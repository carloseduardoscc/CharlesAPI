package com.carlos.charles_api.controller;

import com.carlos.charles_api.dto.OpenServiceOrderRequestDTO;
import com.carlos.charles_api.model.ServiceOrder;
import com.carlos.charles_api.model.dto.OpenServiceOrderRequestDTO;
import com.carlos.charles_api.service.ServiceOrderService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.beans.Transient;
import java.net.URI;

@RestController
@RequestMapping("/workspace/{workspaceId}/serviceorder")
public class ServiceOrderController {

    @Autowired
    ServiceOrderService  service;

    //Puxa o {projectId} do requestMapping para uma variável local, e ser usada para os métodos
    private Long workspaceId;
    @ModelAttribute
    public void setProjectId(@PathVariable("workspaceId") @NotBlank Long projectId) {
        this.workspaceId = projectId;
    }

    @Transactional
    @PostMapping()
    public ResponseEntity open(@RequestBody @Valid OpenServiceOrderRequestDTO soData){
        ServiceOrder os = service.create(soData, workspaceId);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(os.getId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }

}
