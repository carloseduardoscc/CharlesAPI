package com.carlos.charles_api.service;

import com.carlos.charles_api.model.dto.ContactRequestDTO;
import com.carlos.charles_api.model.dto.EmailDTO;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactRequestService {

    @NotNull
    @Autowired
    private final EmailService service;

    @Value("${spring.mail.username}")
    private String supportEmail;

    public void send(ContactRequestDTO request) {
        sendContactRequestEmailToSupport(request);
        sendConfirmationEmailToUser(request);
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

        EmailDTO email = new EmailDTO(
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

        EmailDTO email = new EmailDTO(
                "Charles API backend App",
                supportEmail,
                supportEmail,
                subject,
                body
        );

        service.sendEmail(email);
    }

}
