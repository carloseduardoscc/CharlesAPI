package com.carlos.charles_api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Workspace {
    private Long id;

    private String identification;

    //External
    private List<ServiceOrder> serviceOrders;
    private List<Face> faces;
}
