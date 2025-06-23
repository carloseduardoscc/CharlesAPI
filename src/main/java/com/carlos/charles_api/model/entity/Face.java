package com.carlos.charles_api.model.entity;

import com.carlos.charles_api.model.enums.EntityState;
import com.carlos.charles_api.model.enums.FaceRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "face_tb")
//todo integrar lá no Usuário
public class Face {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //External
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Enumerated(EnumType.STRING)
    private EntityState state;
    @OneToMany(mappedBy = "supporter")
    private List<ServiceOrder> managedSO = new ArrayList<>();
    @OneToMany(mappedBy = "collaborator")
    private List<ServiceOrder> openSO = new ArrayList<>();
    @Enumerated(EnumType.STRING) // Mapeia o valor da enum para uma string ao invés de uma tabela separada no banco de dados
    private FaceRole role;
    @ManyToOne
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    public Face(Long id, User user, EntityState state, FaceRole role, Workspace workspace) {
        this.id = id;
        this.user = user;
        this.state = state;
        this.role = role;
        this.workspace = workspace;
    }

    public Face(Long id, User user, FaceRole role, Workspace workspace) {
        this.id = id;
        this.user = user;
        this.state = EntityState.ACTIVE;
        this.role = role;
        this.workspace = workspace;
    }
}
