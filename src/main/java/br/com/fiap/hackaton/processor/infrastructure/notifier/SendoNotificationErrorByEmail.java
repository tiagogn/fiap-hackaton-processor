package br.com.fiap.hackaton.processor.infrastructure.notifier;

import br.com.fiap.hackaton.processor.core.domain.Notification;
import br.com.fiap.hackaton.processor.core.notifier.SendNotificationError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SendoNotificationErrorByEmail implements SendNotificationError {

    private final MailSender mailSender;

    public SendoNotificationErrorByEmail(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendNotification(Notification notification) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("fiap-processor@gmail.com");
        message.setTo(notification.getTo());
        message.setSubject(notification.getSubject());
        message.setText(notification.getBody());
        mailSender.send(message);
    }
}
