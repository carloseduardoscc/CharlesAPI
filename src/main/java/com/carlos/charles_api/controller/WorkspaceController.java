package com.carlos.charles_api.controller;

import com.carlos.charles_api.dto.response.WorkspaceResponseDTO;
import com.carlos.charles_api.service.WorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("/")
    public ResponseEntity<List<WorkspaceResponseDTO>> listWorkspaces() {
        List<WorkspaceResponseDTO> workspaces = workspaceService.listWorkspacesForCurrentUser();
        return ResponseEntity.ok(workspaces);
    }
}
