package com.carlos.charles_api.model.entity;

import com.carlos.charles_api.model.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "user_tb")
public class User implements UserDetails {

    //Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Internal
    @Column(unique = true, nullable = false)
    private String email;
    private String password;
    private String name;
    private String lastName;

    //External
    @Enumerated(EnumType.STRING)
    // Mapeia o valor da enum para uma string ao invés de uma tabela separada no banco de dados
    private Role role;
    @ManyToOne
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;
    private boolean isEnabled = true;
    @OneToMany(mappedBy = "assignee")
    private List<ServiceOrder> managedSo = new ArrayList<>();
    @OneToMany(mappedBy = "solicitant")
    private List<ServiceOrder> openSo = new ArrayList<>();

    public User(String email, String password, String name, String lastName, Role role, Workspace workspace) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.lastName = lastName;
        this.role = role;
        this.workspace = workspace;
    }

    public String getFullName() {
        return name + " " + lastName;
    }

    public String getIdentification() {
        return role + "/" + email;
    }

    // INFORMAÇÕES PARA AUTENTICAÇÃO E AUTORIZAÇÃO DO SECURITY

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
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
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
