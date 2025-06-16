package com.carlos.charles_api.model.entity;

import com.carlos.charles_api.model.enums.EntityState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "user_tb")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;
    private String password;
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

    public User(String email, String password, String name, String lastName) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.lastName = lastName;
        this.id = null;
        this.state = EntityState.ACTIVE;
    }

    // INFORMAÇÕES PARA AUTENTICAÇÃO E AUTORIZAÇÃO DO SECURITY

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
