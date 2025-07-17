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
import java.util.Objects;

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
    private List<SoState> states = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    private SoStateType currentState = SoStateType.OPEN;

    public ServiceOrder(String soCode, String description, Workspace workspace, User solicitant) {
        this.soCode = soCode;
        this.description = description;
        this.workspace = workspace;
        this.solicitant = solicitant;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ServiceOrder that = (ServiceOrder) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
