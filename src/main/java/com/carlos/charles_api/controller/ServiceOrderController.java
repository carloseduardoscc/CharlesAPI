package com.carlos.charles_api.controller;

import com.carlos.charles_api.model.ServiceOrder;
import com.carlos.charles_api.model.dto.OpenServiceOrderRequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/workspace/{workspaceId}/serviceorder")
public class ServiceOrderController {

    //Puxa o {projectId} do requestMapping para uma variável local, e ser usada para os métodos
    private Long workspaceId;
    @ModelAttribute
    public void setProjectId(@PathVariable("workspaceId") @NotBlank Long projectId) {
        this.workspaceId = projectId;
    }

    @PostMapping("/open")
    public ResponseEntity open(@RequestBody @Valid OpenServiceOrderRequestDTO so){
        return ResponseEntity.ok("Chamado aberto no workspace id="+workspaceId+" \nService order:\n"+so);
    }

}
