package com.labhesh.Todos.Todos.app.authentication;

import com.labhesh.Todos.Todos.app.user.Users;
import com.labhesh.Todos.Todos.exception.InternalServerException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public void sendMail(String to, String subject, String text) throws InternalServerException {
        try {
            MimeMessage email = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(email, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true); // Set to true for HTML content
            mailSender.send(email);
        } catch (Exception e) {
            System.out.println("Error sending email: " + e.getMessage());
            throw new InternalServerException(e.getMessage());
        }
    }

    public void sendVerificationEmail(Users user) throws InternalServerException {
        String subject = "Todos :: Email Verification";
        String mailContent = "<p>Please click the link below to verify your email:</p>";
        String verifyURL = "http://localhost:8088/verify?token=" + user.getVerificationToken();
        mailContent += "<a href=\"" + verifyURL + "\">Verify</a>";
        sendMail(user.getEmail(), subject, mailContent);
    }

    public void sendResetPasswordEmail(Users user) throws InternalServerException {
        String subject = "Password Reset";
        String mailContent = "<p>Please click the link below to reset your password:</p>";
        String resetURL = "http://localhost:8088/reset-password?token=" + user.getResetPasswordToken();
        mailContent += "<a href=\"" + resetURL + "\">Reset Password</a>";
        sendMail(user.getEmail(), subject, mailContent);
    }
}
