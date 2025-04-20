package br.com.fiap.hackaton.processor.infrastructure.notifier;

import br.com.fiap.hackaton.processor.core.domain.Notification;
import br.com.fiap.hackaton.processor.core.notifier.SendNotificationError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SendoNotificationErrorByEmail implements SendNotificationError {

    private final MailSender mailSender;

    @Value("${aws.ses.email-from}")
    private String defaultFrom;

    public SendoNotificationErrorByEmail(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    @Override
    public void sendNotification(Notification notification) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(defaultFrom);
        message.setTo(notification.getTo());
        message.setSubject(notification.getSubject());
        message.setText(notification.getBody());
        mailSender.send(message);
    }
}
