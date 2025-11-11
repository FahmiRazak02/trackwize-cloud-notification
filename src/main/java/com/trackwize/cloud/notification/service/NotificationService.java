package com.trackwize.cloud.notification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trackwize.cloud.notification.constant.ErrorConst;
import com.trackwize.cloud.notification.constant.NotificationConst;
import com.trackwize.cloud.notification.exception.TrackWizeException;
import com.trackwize.cloud.notification.model.dto.NotificationReqDTO;
import jakarta.jms.JMSException;
import jakarta.jms.TextMessage;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final EmailService emailService;
    private final InboxService inboxService;

    public void processQueueMessage(TextMessage message) throws JMSException, JsonProcessingException, TrackWizeException, MessagingException {
        ObjectMapper mapper = new ObjectMapper();

        NotificationReqDTO reqDTO = mapper.readValue(message.getText(), NotificationReqDTO.class);
        log.info("Template: {}", reqDTO.getTemplate());
        log.info("Notification Type: {}", reqDTO.getNotificationType());
        log.info("NotificationReqDTO: [{}]", reqDTO);

        switch (reqDTO.getNotificationType()) {
            case NotificationConst.EMAIL_NTF_TYPE:
                emailService.sendEmail(reqDTO);
                break;
            case NotificationConst.INBOX_NTF_TYPE:
                inboxService.createInbox(reqDTO);
                break;
            default:
                log.info("Invalid Notification Type");
                throw new TrackWizeException(
                        ErrorConst.INVALID_TYPE_CODE,
                        ErrorConst.INVALID_TYPE_MSG
                );
        }
    }
}
