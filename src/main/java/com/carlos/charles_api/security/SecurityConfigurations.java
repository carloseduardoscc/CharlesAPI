package com.carlos.charles_api.security;

import com.carlos.charles_api.controller.exceptionHandler.ResourceExceptionHandler;
import com.carlos.charles_api.model.enums.FaceRole;
import com.carlos.charles_api.security.exceptions.CustomAccessDeniedHandler;
import com.carlos.charles_api.security.exceptions.CustomAuthenticationEntryPoint;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    CustomAccessDeniedHandler accessDeniedHandler;
    @Autowired
    CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    ResourceExceptionHandler excHandler;

    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    RoleAuthorizationFilter roleAuthorizationFilter;

    @Value("${api.security.cors.allowed_origins}")
    String allowedOrigins;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        try {
            return  httpSecurity
                    .headers(headers -> headers // Permite uso de iframes (necessário para o H2 Console)
                            .frameOptions(frame -> frame.disable())
                    )
                    .csrf(csrf -> csrf.disable())
                    // Configuração de CORS do Security, ele tem um aparte do padrão do Spring que precisa também ser configurado
                    .cors(cors -> cors.configurationSource(request -> {
                        var corsConfig = new org.springframework.web.cors.CorsConfiguration();
                        corsConfig.setAllowedOriginPatterns(List.of(allowedOrigins));
                        corsConfig.setAllowedMethods(List.of("*"));
                        corsConfig.setAllowedHeaders(List.of("*"));
                        corsConfig.setAllowCredentials(true);
                        return corsConfig;
                    }))
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authorizeHttpRequests(authorize -> authorize
                            .requestMatchers("/h2-console/**").permitAll()
                            .requestMatchers("/auth/login").permitAll()
                            .requestMatchers("/auth/register").permitAll()
                            .requestMatchers("/contactRequest/send").permitAll()
                            .requestMatchers(HttpMethod.POST, "/workspace/{workspaceId}/serviceorder/open").hasRole(FaceRole.COLLABORATOR.toString())
                            .anyRequest().authenticated()
                    )
                    // Mapeia as exceções de autenticação e autorização do security para as classes customizadas que usam o ResourceExceptionHandler
                    .exceptionHandling(ex -> ex
                            .accessDeniedHandler(accessDeniedHandler)
                            .authenticationEntryPoint(authenticationEntryPoint)
                    )
                    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) //filtro de autenticação do usuário
                    .addFilterBefore(roleAuthorizationFilter, UsernamePasswordAuthenticationFilter.class) //filtro que verifica autorização por roles
                    .build();
//        } catch (AccessDeniedException e) {
//            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//            excHandler.accessDeniedException(e, request);
//        }
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
