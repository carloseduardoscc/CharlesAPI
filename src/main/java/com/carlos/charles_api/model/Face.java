package com.carlos.charles_api.model;

import com.carlos.charles_api.model.enums.EntityState;
import com.carlos.charles_api.model.enums.FaceRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Entity(name = "face_tb")
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
    //todo o managedSOs é dos supporters e admin, e o openSOs do collaborators, na hora de voltar json mostrar apenas o respectivo de cada um
    @OneToMany(mappedBy = "supporter")
    private List<ServiceOrder> managedSO;
    @OneToMany(mappedBy = "collaborator")
    private List<ServiceOrder> openSO;
    @Enumerated(EnumType.STRING)
    private FaceRole role;
    @ManyToOne
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;
}
