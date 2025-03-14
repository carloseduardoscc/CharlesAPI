package com.carlos.charles_api.model;

import com.carlos.charles_api.model.enums.EntityState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.autoconfigure.web.WebProperties;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Entity(name = "user_tb")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String userId;
    private String name;
    private String lastName;

    //External
    @Enumerated(EnumType.STRING)
    private EntityState state;
    @OneToMany(mappedBy = "user")
    private List<Face> faces = new ArrayList<>();

    public User(Long id, String userId, String name, String lastName, EntityState state) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.lastName = lastName;
        this.state = state;
    }
}
