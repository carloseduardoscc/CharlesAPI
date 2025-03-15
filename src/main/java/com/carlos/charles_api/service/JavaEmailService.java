package com.carlos.charles_api.service;

import com.carlos.charles_api.dto.EmailDTO;
import com.carlos.charles_api.service.exceptions.EmailException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JavaEmailService implements EmailService {

    @Autowired
    private final JavaMailSender sender;

    public void sendEmail(EmailDTO emailDTO){

        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(emailDTO.getFrom());
            helper.setTo(emailDTO.getTo());
            helper.setSubject(emailDTO.getSubject());
            helper.setText(emailDTO.getBody(), true);

            sender.send(message);
        } catch (MessagingException e) {
            throw new EmailException(e.getMessage());
        }
    }


}
