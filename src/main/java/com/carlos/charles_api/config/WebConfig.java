package com.carlos.charles_api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Classe de configuração responsável por definir as regras de CORS para a aplicação.
 */
@Configuration // Informa ao Spring que esta classe possui configurações.
public class WebConfig implements WebMvcConfigurer {

    // Recupera do application.properties ou application.yml os domínios permitidos para acessar a API.
    @Value("${api.security.cors.allowed_origins}")
    String allowedOrigins;

    /**
     * Método que define as configurações de CORS:
     * - O padrão "/**" indica que todas as rotas (endpoints) estão cobertas.
     * - allowedMethods, allowedHeaders como "*" permitem qualquer método/cabeçalho.
     * - allowedOriginPatterns usa o valor configurado para permitir ou negar origens dinamicamente.
     * - allowCredentials(true) habilita o envio de cookies, headers de autenticação, etc.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Aplica CORS em todos os endpoints
                .allowedMethods("*") // Permite todos os métodos HTTP (GET, POST, etc)
                .allowedHeaders("*") // Permite qualquer cabeçalho
                .allowedOriginPatterns(allowedOrigins) // Define as origens a partir da propriedade
                .allowCredentials(true); // Permite envio de credenciais (cookies, tokens)
    }
}