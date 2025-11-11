package com.trackwize.cloud.notification.activemq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.trackwize.cloud.notification.activemq.base.JmsListenerBase;
import com.trackwize.cloud.notification.exception.TrackWizeException;
import com.trackwize.cloud.notification.service.NotificationService;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ActiveMQReceiver extends JmsListenerBase {

    private final NotificationService notificationService;

    @JmsListener(destination = "${spring.artemis.queues.email}", containerFactory = "jmsListenerContainerFactory")
    public void handleEmail(TextMessage message) throws Exception {
        log.info("[EmailQueue] Received: {}", message.getText());
        log.info("[EmailQueue] CorrelationID: {}", message.getJMSCorrelationID());
        super.handle(message);
    }

    @JmsListener(destination = "${spring.artemis.queues.inbox}", containerFactory = "jmsListenerContainerFactory")
    public void handleInbox(TextMessage message) throws Exception {
        log.info("[InboxQueue] Received: {}", message.getText());
        log.info("[InboxQueue] CorrelationID: {}", message.getJMSCorrelationID());
        super.handle(message);
    }

    @Override
    protected void onMessage(Message message) throws JMSException, TrackWizeException, MessagingException, JsonProcessingException {
        notificationService.processQueueMessage((TextMessage) message);
    }
}
