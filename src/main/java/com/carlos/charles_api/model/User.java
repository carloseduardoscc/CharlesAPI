package com.carlos.charles_api.model;

import com.carlos.charles_api.model.enums.EntityState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "user_tb")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;
    private String name;
    private String lastName;

    //External
    @Enumerated(EnumType.STRING)
    private EntityState state;
    @OneToMany(mappedBy = "user")
    private List<Face> faces = new ArrayList<>();

    public User(Long id, String email, String name, String lastName, EntityState state) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.lastName = lastName;
        this.state = state;
    }

    public User(Long id, String email, String name, String lastName) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.lastName = lastName;
        this.state = EntityState.ACTIVE;
    }
}
