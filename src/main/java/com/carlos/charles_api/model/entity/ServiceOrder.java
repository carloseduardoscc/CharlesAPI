package com.carlos.charles_api.model.entity;

import com.carlos.charles_api.dto.request.OpenServiceOrderRequestDTO;
import com.carlos.charles_api.model.enums.SoStateType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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
    @JoinColumn(name = "assignee_id")
    private User assignee;
    @ManyToOne
    @JoinColumn(name = "solicitant_id")
    private User solicitant;
    @OneToMany(mappedBy = "serviceOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SoState> states = new ArrayList<>(List.of(new SoState(null, LocalDateTime.now(), SoStateType.OPEN, this)));
    @Enumerated(EnumType.STRING)
    private SoStateType currentState = SoStateType.OPEN;

    public ServiceOrder(String soCode, String description, Workspace workspace, User solicitant) {
        this.soCode = soCode;
        this.description = description;
        this.workspace = workspace;
        this.solicitant = solicitant;
    }
}
