package com.carlos.charles_api.service;

import com.carlos.charles_api.model.dto.EmailDTO;

public interface EmailService {
    void sendEmail(EmailDTO messageDto);
}
