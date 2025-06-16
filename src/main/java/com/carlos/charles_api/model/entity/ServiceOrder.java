package com.carlos.charles_api.model.entity;

import com.carlos.charles_api.model.enums.SoStateType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "serviceOrder_tb")
public class ServiceOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String soCode;
    private String description;
    private String diagnostic;

    //External
    @ManyToOne
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;
    @ManyToOne
    @JoinColumn(name = "supporter_id")
    private Face supporter;
    @ManyToOne
    @JoinColumn(name = "collaborator_id")
    private Face collaborator;
    @OneToMany(mappedBy = "serviceOrder")
    private List<SoState> states = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    private SoStateType currentState;

    public ServiceOrder(Long id, String soCode, String description, String diagnostic, Workspace workspace, Face supporter, Face collaborator, SoStateType currentState) {
        this.id = id;
        this.soCode = soCode;
        this.description = description;
        this.diagnostic = diagnostic;
        this.workspace = workspace;
        this.supporter = supporter;
        this.collaborator = collaborator;
        this.currentState = currentState;
    }

    public ServiceOrder(Long id, String soCode, String description, String diagnostic, Workspace workspace, Face supporter, Face collaborator) {
        this.id = id;
        this.soCode = soCode;
        this.description = description;
        this.diagnostic = diagnostic;
        this.workspace = workspace;
        this.supporter = supporter;
        this.collaborator = collaborator;
        this.currentState = SoStateType.ASSIGNED;
    }

    public ServiceOrder(Long id, String soCode, String description, Workspace workspace, Face collaborator) {
        this.id = id;
        this.soCode = soCode;
        this.description = description;
        this.workspace = workspace;
        this.collaborator = collaborator;
        this.currentState = SoStateType.ASSIGNED;
    }
}
