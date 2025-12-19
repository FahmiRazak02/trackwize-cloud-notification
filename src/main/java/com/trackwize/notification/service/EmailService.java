package com.trackwize.notification.service;

import com.trackwize.notification.constant.NotificationConst;
import com.trackwize.notification.model.dto.NotificationReqDTO;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Properties;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${smtp.port}")
    private String smtpPort;

    @Value("${smtp.host}")
    private String smtpHost;

    @Value("${smtp.username}")
    private String smtpUsername;

    @Value("${smtp.password}")
    private String smtpPassword;

    public void sendEmail(NotificationReqDTO reqDTO) throws MessagingException {

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", smtpPort);
        properties.put("mail.smtp.ssl.trust", smtpHost);

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(smtpUsername, smtpPassword);
            }
        });

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(smtpUsername));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(reqDTO.getRecipient()));
        message.setSubject(reqDTO.getSubject());

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(contentsToHtml(reqDTO), "text/html; charset=utf-8");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);
        message.setContent(multipart);

        Transport.send(message);
    }

    public String contentsToHtml(NotificationReqDTO reqDTO) {
        if (reqDTO.getContents() == null || reqDTO.getContents().isEmpty()) {
            return "";
        }

        String content = "";
        switch (reqDTO.getTemplate()) {
            case NotificationConst.PASSWORD_RESET_TEMPLATE:
                content =  resetPasswordContent(reqDTO.getContents());
        }

        return content;
    }

    public String resetPasswordContent(Map<String, Object> contents) {
        String title = (String) contents.getOrDefault("title", "Notification");
        String message = (String) contents.getOrDefault("message", "Please review the details below.");
        String token = (String) contents.get("token");
        String link = "http://localhost:8080/auth/reset-password/process?token=" + token;
        String expiry = String.valueOf(contents.getOrDefault("expiry", "10"));

        StringBuilder sb = new StringBuilder();
        sb.append("<html><body>")
                .append("<h3>").append(title).append("</h3>")
                .append("<p>").append(message).append("</p>");

        if (token != null && !token.isEmpty()) {
            sb.append("<p><a href=\"").append(link).append("\">Reset Password</a></p>");
        }

        sb.append("<br><p>This link will expire in ").append(expiry).append(" minutes.</p>")
                .append("</body></html>");

        return sb.toString();
    }
}
