package com.carlos.charles_api.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "workspace_tb")
public class Workspace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String identification;

    //External
    @OneToMany(mappedBy = "workspace")
    private List<ServiceOrder> serviceOrders = new ArrayList<>();
    @OneToMany(mappedBy = "workspace")
    private List<User> users = new ArrayList<>();

    public Workspace(Long id, String identification) {
        this.id = id;
        this.identification = identification;
    }

    public Workspace(String identification) {
        this.identification = identification;
    }
}
