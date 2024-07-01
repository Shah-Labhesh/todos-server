package com.labhesh.Todos.Todos.app.authentication;

import com.labhesh.Todos.Todos.exception.InternalServerException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public void sendMail(String to, String subject, String text) throws InternalServerException {


        JavaMailSenderImpl senderImpl = (JavaMailSenderImpl) mailSender;
        Properties properties = senderImpl.getJavaMailProperties();

        try {
            Session session = Session.getInstance(properties);
            MimeMessage email = new MimeMessage(session);
            email.setFrom(from);
            email.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(to));
            email.setSubject(subject);
            MimeMultipart multipart = new MimeMultipart("related");
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(text, "text/html; charset=utf-8");
            multipart.addBodyPart(htmlPart);
            email.setContent(multipart);
            Transport transport = session.getTransport(senderImpl.getProtocol());
            transport.connect(senderImpl.getHost(), senderImpl.getPort(), senderImpl.getUsername(), senderImpl.getPassword());
            transport.sendMessage(email, email.getAllRecipients());
            transport.close();
        } catch (Exception e) {
            System.out.println("Error sending email: " + e.getMessage());
            throw new InternalServerException(e.getMessage());
        }


    }
}
