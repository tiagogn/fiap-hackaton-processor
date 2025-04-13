package br.com.fiap.hackaton.processor.core.notifier;

import br.com.fiap.hackaton.processor.core.domain.Notification;

public interface SendNotificationError {
    void sendNotification(Notification notification);
}
