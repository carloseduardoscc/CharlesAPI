package com.carlos.charles_api.controller;

import com.carlos.charles_api.dto.request.WorkspaceRequestDTO;
import com.carlos.charles_api.dto.response.WorkspaceResponseDTO;
import com.carlos.charles_api.service.WorkspaceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/workspace")
public class WorkspaceController {

    @Autowired
    private WorkspaceService workspaceService;

    /**
     * Lists all workspaces that the authenticated user is part of
     * @return List of workspace DTOs
     */
    //todo não é mais necessário listar workspaces, o acesso é direto
    @GetMapping("/")
    public ResponseEntity<List<WorkspaceResponseDTO>> listWorkspaces() {
        List<WorkspaceResponseDTO> workspaces = workspaceService.listWorkspacesForCurrentUser();
        return ResponseEntity.ok(workspaces);
    }

    /**
     * Creates a new workspace with the authenticated user as owner
     * @param workspaceRequestDTO The workspace data
     * @return The created workspace
     */
    @PostMapping("/")
    public ResponseEntity<WorkspaceResponseDTO> createWorkspace(@Valid @RequestBody WorkspaceRequestDTO workspaceRequestDTO) {
        WorkspaceResponseDTO createdWorkspace = workspaceService.createWorkspace(workspaceRequestDTO);

        // Create URI for the created resource
        var uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdWorkspace.id())
                .toUri();

        // Return 201 Created status with the created workspace and location header
        return ResponseEntity.created(uri).body(createdWorkspace);
    }
}
