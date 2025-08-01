package com.carlos.charles_api.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Value("${api.security.cors.allowed_origins}")
    String allowedOrigins;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        try {
            return  httpSecurity
                    .headers(headers -> headers // Permite uso de iframes (necessário para o H2 Console)
                            .frameOptions(frame -> frame.disable())
                    )
                    // Desabilita o padrão de segurança csrf
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
                            .requestMatchers("/", "/Login.html", "/tickets-and-transfer.html", "/index.html", "/Home.html", "/call-details.html","/Cadastro.html", "/css/**", "/Images/**", "/javascript/**").permitAll()
                            .requestMatchers("/h2-console/**").permitAll()
                            .requestMatchers("/auth/login").permitAll()
                            .requestMatchers("/auth/register").permitAll()
                            .requestMatchers("/contactRequest/send").permitAll()
                            .anyRequest().authenticated()
                    )
                    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) //filtro de autenticação do usuário
                    .build();
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
