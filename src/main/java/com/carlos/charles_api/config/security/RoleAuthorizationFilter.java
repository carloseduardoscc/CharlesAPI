package com.carlos.charles_api.config.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.carlos.charles_api.model.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//todo Provavelmente não será mais útil, a lógica de mapear as roles para os usuários vai ser direto nos override da entidade
@Component
public class RoleAuthorizationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RoleAuthorizationFilter.class);

    @Autowired
    private FaceRepository faceRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //se o usuário estiver autenticado atualiza as permissões baseadas no workspace correto
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();

            String path = request.getRequestURI();
            Long projectId = extractProjectIdFromPath(path);

            List<String> roles = getUserRolesInWorkspace(user, projectId);

            updateUserAuthorities(roles);

            log.debug("Path {} acessed by user {} with {} authority", path, user.getEmail(), roles.toArray());
        }

        //senão continua a processar
        filterChain.doFilter(request, response);
    }

    public void updateUserAuthorities(List<String> roles) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {

            List<GrantedAuthority> authorities = new ArrayList<>();

            for (String role : roles) {
                authorities.add(new SimpleGrantedAuthority(role));
            }

            Authentication newAuth = new UsernamePasswordAuthenticationToken(
                    authentication.getPrincipal(),
                    authentication.getCredentials(),
                    authorities
            );

            SecurityContextHolder.getContext().setAuthentication(newAuth);
        }
    }

    private List<String> getUserRolesInWorkspace(User user, Long projectId) {
        return faceRepository.findByUserId(user.getId()).stream()
                .filter(f -> f.getWorkspace().getId().equals(projectId)) //filtra as faces relacionadas ao workspace certo
                .map(f -> "ROLE_" + f.getRole().toString()) //mapeia para obter a autorização do usuário nesse workspace
                .collect(Collectors.toList());
    }

    private Long extractProjectIdFromPath(String path) {
        //quebra o path pelos '/' e pega o 3° segmento que contém o id do workspace
        //deve ser alterado se o caminho mudar!
        String[] segments = path.split("/");
        try {
            return Long.parseLong(segments[2]);
        } catch (Exception e) {
            return null;
        }
    }
}

