package com.carlos.charles_api.controller;

import com.carlos.charles_api.dto.request.OpenServiceOrderDTO;
import com.carlos.charles_api.dto.response.ServiceOrderResponseDTO;
import com.carlos.charles_api.model.entity.ServiceOrder;
import com.carlos.charles_api.service.ServiceOrderService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

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
    public ResponseEntity open(@RequestBody @Valid OpenServiceOrderDTO soData){
        ServiceOrder os = service.create(soData, workspaceId);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(os.getId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    /**
     * Lists all service orders for a specific workspace based on user's role
     * - Collaborators can only see service orders they opened
     * - Supporters, Admins, and Owners can see all service orders in the workspace
     *
     * @param workspaceId The ID of the workspace
     * @return List of service order DTOs
     */
    @GetMapping("/")
    public ResponseEntity<List<ServiceOrderResponseDTO>> listServiceOrders() {
        List<ServiceOrderResponseDTO> serviceOrders = service.list(workspaceId);
        return ResponseEntity.ok(serviceOrders);
    }
}
