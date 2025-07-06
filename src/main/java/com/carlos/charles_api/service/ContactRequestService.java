package com.carlos.charles_api.service;

import com.carlos.charles_api.dto.request.ContactRequestDTO;
import com.carlos.charles_api.exceptions.EmailException;
import com.carlos.charles_api.model.EmailData;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactRequestService {

    @NotNull
    @Autowired
    private final EmailService service;

    @Value("${spring.mail.username}")
    private String supportEmail;

    private static final Logger logger = LoggerFactory.getLogger("ACCESS_LOGGER");

    @Async
    public void send(ContactRequestDTO request) {
        try {
            sendContactRequestEmailToSupport(request);
            sendConfirmationEmailToUser(request);
            logger.atInfo().log("Email enviado com sucesso para o email {} e para o email de suporte {}.", request.email(), supportEmail);
        } catch (Exception e) {
            throw new EmailException(e.getMessage());
        }
    }

    private void sendConfirmationEmailToUser(ContactRequestDTO requestDto) {

        String body = String.format("<html>\n" +
                "  <body>\n" +
                "    <h2>Olá, %s!</h2>\n" +
                "    <p>Recebemos sua solicitação de contato com a nossa equipe. Agradecemos por entrar em contato!</p>\n" +
                "    <p><strong>Status:</strong> Sua solicitação está sendo processada e nossa equipe de suporte entrará em contato com você em breve.</p>\n" +
                "    <p>Enquanto isso, fique à vontade para navegar pela nossa plataforma e conhecer mais sobre nossos serviços.</p>\n" +
                "    <p><strong>O que acontecerá a seguir?</strong></p>\n" +
                "    <ul>\n" +
                "      <li>Nossa equipe analisará sua solicitação.</li>\n" +
                "      <li>Você receberá uma resposta com a solução ou um encaminhamento do seu pedido.</li>\n" +
                "    </ul>\n" +
                "    <p>Atenciosamente,</p>\n" +
                "    <p><strong>Equipe Charles²</strong></p>\n" +
                "  </body>\n" +
                "</html>\n", requestDto.name()
        );

        EmailData email = new EmailData(
                "Charles support team",
                supportEmail,
                requestDto.email(),
                "Sua solicitação foi recebida com sucesso!",
                body
        );

        service.sendEmail(email);
    }

    private void sendContactRequestEmailToSupport(ContactRequestDTO requestDto) {
        String subject = "Novo pedido de contato recebido";
        String body = "<html><body>"
                + "<h2>Nova Solicitação de Contato</h2>"
                + "<p><strong>Nome:</strong> " + requestDto.name() + "</p>"
                + "<p><strong>Telefone:</strong> " + requestDto.phone() + "</p>"
                + "<p><strong>Email:</strong> " + requestDto.email() + "</p>"
                + "<p><strong>Cidade:</strong> " + requestDto.city() + "</p>"
                + "<p><strong>Tipo de Pessoa:</strong> " + requestDto.personType() + "</p>"
                + "<p><strong>Mensagem:</strong><br>" + requestDto.message() + "</p>"
                + "</body></html>";

        EmailData email = new EmailData(
                "Charles API backend App",
                supportEmail,
                supportEmail,
                subject,
                body
        );

        service.sendEmail(email);
    }

}
