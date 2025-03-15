package com.carlos.charles_api.config;

import com.carlos.charles_api.service.EmailService;
import com.carlos.charles_api.service.JavaEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;

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
