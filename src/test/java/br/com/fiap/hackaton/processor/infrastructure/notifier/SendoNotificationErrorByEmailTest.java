package br.com.fiap.hackaton.processor.infrastructure.notifier;

import br.com.fiap.hackaton.processor.core.domain.Notification;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SendoNotificationErrorByEmailTest {

    @Test
    void shouldSendNotificationEmail() {
        // Mock do MailSender
        MailSender mailSender = Mockito.mock(MailSender.class);

        // Instância da classe a ser testada
        SendoNotificationErrorByEmail notifier = new SendoNotificationErrorByEmail(mailSender);

        // Criação do objeto Notification
        Notification notification = Notification.builder()
                .to("test@example.com")
                .subject("Test Subject")
                .body("Test Body")
                .build();

        // Execução do método
        notifier.sendNotification(notification);

        // Captura do argumento enviado ao MailSender
        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(captor.capture());

        // Validação dos valores do e-mail enviado
        SimpleMailMessage sentMessage = captor.getValue();
        assertEquals("fiap-processor@gmail.com", sentMessage.getFrom());
        assertEquals("test@example.com", Objects.requireNonNull(sentMessage.getTo())[0]);
        assertEquals("Test Subject", sentMessage.getSubject());
        assertEquals("Test Body", sentMessage.getText());
    }
}

