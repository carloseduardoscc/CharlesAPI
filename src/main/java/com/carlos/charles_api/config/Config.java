package com.carlos.charles_api.config;

import com.carlos.charles_api.service.EmailService;
import com.carlos.charles_api.service.JavaEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * Classe de configuração principal do projeto, responsável por fornecer beans personalizados.
 */
@Configuration // Informa ao Spring que esta classe possui métodos para registrar beans.
public class Config {

    @Autowired
    JavaMailSender javaMailSender; // Injeta o objeto responsável pelo envio de e-mails SMTP.

    /**
     * Cria e registra o bean principal (primary) de EmailService.
     * Sempre que EmailService for requisitado, retorna uma instância de JavaEmailService,
     * utilizando o JavaMailSender já configurado.
     */
    @Bean
    @Primary // Este bean será preferido caso existam outros do mesmo tipo.
    public EmailService emailService(){
        return new JavaEmailService(javaMailSender);
    }
}