package com.trackwize.notification.activemq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.trackwize.common.exception.TrackWizeException;
import com.trackwize.common.jms.JmsListenerBase;
import com.trackwize.common.util.LogUtil;
import com.trackwize.notification.model.dto.NotificationReqDTO;
import com.trackwize.notification.service.NotificationService;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ActiveMQReceiver extends JmsListenerBase {

    private final NotificationService notificationService;

    @JmsListener(destination = "${spring.artemis.queues.email}", containerFactory = "jmsListenerContainerFactory")
    public void handleEmail(TextMessage message) throws Exception {
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

        log.info(LogUtil.repeatCharLine('=', 117));
        log.info("[EmailQueue] Received    : {}", "ntf_email_queue");
        log.info("[EmailQueue] trackingId  : {}", message.getJMSCorrelationID());
        log.info(LogUtil.repeatCharLine('=', 117));

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
