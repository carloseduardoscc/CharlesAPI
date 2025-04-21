package com.carlos.charles_api.config;

import com.carlos.charles_api.service.EmailService;
import com.carlos.charles_api.service.JavaEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Config {

    @Autowired
    JavaMailSender javaMailSender;

    @Bean
    @Primary
    public EmailService emailService(){
        return new JavaEmailService(javaMailSender);
    }


}
