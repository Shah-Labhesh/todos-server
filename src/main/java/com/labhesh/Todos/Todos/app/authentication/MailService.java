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

    @Value("${app.base-url}")
    private String baseUrl;

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
        String verifyURL = baseUrl + "/verify?token=" + user.getVerificationToken();
        String mailContent =
                "<html>" +
                        "<head>" +
                        "<style>" +
                        "body { font-family: Arial, sans-serif; background-color: #f5f5f5; margin: 0; padding: 0; }" +
                        ".container { background-color: #ffffff; padding: 20px; margin: 0 auto; max-width: 600px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); }" +
                        ".header { font-size: 24px; font-weight: bold; margin-bottom: 20px; color: #333333; }" +
                        ".content { font-size: 16px; line-height: 1.5; color: #555555; }" +
                        ".button { display: inline-block; padding: 10px 20px; font-size: 16px; color: #ffffff; background-color: #3498db; border-radius: 5px; text-decoration: none; }" +
                        ".footer { margin-top: 20px; font-size: 12px; color: #aaaaaa; }" +
                        "</style>" +
                        "</head>" +
                        "<body>" +
                        "<div class='container'>" +
                        "<div class='header'>Email Verification</div>" +
                        "<div class='content'>" +
                        "<p>Thank you for registering. Please click the button below to verify your email address:</p>" +
                        "<a href='" + verifyURL + "' class='button'>Verify Email</a>" +
                        "<p>If the button above does not work, copy and paste the following link into your browser:</p>" +
                        "<p><a href='" + verifyURL + "'>" + verifyURL + "</a></p>" +
                        "</div>" +
                        "<div class='footer'>If you did not request this email, please ignore it.</div>" +
                        "</div>" +
                        "</body>" +
                        "</html>";
        sendMail(user.getEmail(), subject, mailContent);
    }

    public void sendResetPasswordEmail(Users user) throws InternalServerException {
        String subject = "Password Reset";
        String resetURL = baseUrl + "/reset-password?token=" + user.getResetPasswordToken();
        String mailContent =
                "<html>" +
                        "<head>" +
                        "<style>" +
                        "body { font-family: Arial, sans-serif; background-color: #f5f5f5; margin: 0; padding: 0; }" +
                        ".container { background-color: #ffffff; padding: 20px; margin: 0 auto; max-width: 600px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); }" +
                        ".header { font-size: 24px; font-weight: bold; margin-bottom: 20px; color: #333333; }" +
                        ".content { font-size: 16px; line-height: 1.5; color: #555555; }" +
                        ".button { display: inline-block; padding: 10px 20px; font-size: 16px; color: #ffffff; background-color: #e74c3c; border-radius: 5px; text-decoration: none; }" +
                        ".footer { margin-top: 20px; font-size: 12px; color: #aaaaaa; }" +
                        "</style>" +
                        "</head>" +
                        "<body>" +
                        "<div class='container'>" +
                        "<div class='header'>Password Reset</div>" +
                        "<div class='content'>" +
                        "<p>You requested to reset your password. Please click the button below to reset it:</p>" +
                        "<a href='" + resetURL + "' class='button'>Reset Password</a>" +
                        "<p>If the button above does not work, copy and paste the following link into your browser:</p>" +
                        "<p><a href='" + resetURL + "'>" + resetURL + "</a></p>" +
                        "</div>" +
                        "<div class='footer'>If you did not request this email, please ignore it.</div>" +
                        "</div>" +
                        "</body>" +
                        "</html>";
        sendMail(user.getEmail(), subject, mailContent);
    }

}
