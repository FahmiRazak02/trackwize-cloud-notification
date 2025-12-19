package com.trackwize.notification.util;

import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ActiveMQUtil {

    private final JmsTemplate jmsTemplate;

    @Value("${spring.artemis.receive-timeout:5000}")
    private long receiveTimeout;

    public void send(String correlationId, String destination, String content) {
        log.info("[JMS-SEND] messageId={} destination={} content={}", correlationId, destination, content);
        try {
            jmsTemplate.send(destination, session -> {
                TextMessage message = session.createTextMessage(content);
                message.setJMSCorrelationID(correlationId);
                return message;
            });
            log.info("[JMS-SEND] Successfully sent to {}", destination);
        } catch (Exception e) {
            log.error("[JMS-SEND] Failed to send to {}: {}", destination, e.getMessage(), e);
        }
    }

    public String sendAndAwait(String correlationId, String destination, String content) {
        log.info("[JMS-SEND-AWAIT] messageId={} destination={} content={}", correlationId, destination, content);
        try {
            Message reply = jmsTemplate.sendAndReceive(destination, session -> {
                TextMessage message = session.createTextMessage(content);
                message.setJMSCorrelationID(correlationId);
                return message;
            });

            if (reply == null) {
                log.warn("[JMS-SEND-AWAIT] No reply within timeout ({} ms)", receiveTimeout);
                return null;
            }

            if (reply instanceof TextMessage textReply) {
                String response = textReply.getText();
                log.info("[JMS-SEND-AWAIT] Received reply: {}", response);
                return response;
            }

            log.warn("[JMS-SEND-AWAIT] Unexpected reply type: {}", reply.getClass().getName());
            return null;

        } catch (Exception e) {
            log.error("[JMS-SEND-AWAIT] Error sending or waiting for reply: {}", e.getMessage(), e);
            return null;
        }
    }

    public void reply(Message originalMessage, String content) {
        try {
            String replyTo = originalMessage.getJMSReplyTo() != null
                    ? originalMessage.getJMSReplyTo().toString()
                    : "UNKNOWN";

            log.info("[JMS-REPLY] replyingTo={} messageId={} content={}",
                    replyTo, originalMessage.getJMSMessageID(), content);

            jmsTemplate.send(originalMessage.getJMSReplyTo(), session -> {
                TextMessage response = session.createTextMessage(content);
                response.setJMSCorrelationID(originalMessage.getJMSMessageID());
                return response;
            });

            log.info("[JMS-REPLY] Successfully sent reply to {}", replyTo);

        } catch (Exception e) {
            log.error("[JMS-REPLY] Failed to send reply: {}", e.getMessage(), e);
        }
    }
}
