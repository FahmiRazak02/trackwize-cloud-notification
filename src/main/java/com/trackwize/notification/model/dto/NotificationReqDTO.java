package com.trackwize.notification.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationReqDTO {

    private String trackingId;

    private char notificationType;
    private int template;
    private String recipient;
    private String subject;
    private Map<String, Object> contents;
}
