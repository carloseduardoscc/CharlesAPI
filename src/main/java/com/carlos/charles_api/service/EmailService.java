package com.carlos.charles_api.service;

import com.carlos.charles_api.model.EmailData;

public interface EmailService {
    void sendEmail(EmailData messageDto);
}
