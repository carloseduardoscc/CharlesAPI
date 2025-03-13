package com.carlos.charles_api.model;

import com.carlos.charles_api.model.enums.SoStateType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
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
    @OneToMany
    private List<SoState> states;
    @Enumerated(EnumType.STRING)
    private SoStateType currentState;

}
