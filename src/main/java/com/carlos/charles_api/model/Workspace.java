package com.carlos.charles_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Entity(name = "workspace_tb")
public class Workspace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String identification;

    //External
    @OneToMany(mappedBy = "workspace")
    private List<ServiceOrder> serviceOrders;
    @OneToMany(mappedBy = "workspace")
    private List<Face> faces;
}
