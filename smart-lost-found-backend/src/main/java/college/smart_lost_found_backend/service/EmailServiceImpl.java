package college.smart_lost_found_backend.service;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;


    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    private String buildHtmlTemplate(String itemName, String messageBody, String phoneNumber) {

        return """
    <html>
    <body style="font-family: Arial, sans-serif; background-color: #f4f6f8; padding: 20px;">

        <div style="max-width: 600px; margin: auto; background: white; padding: 20px; border-radius: 10px;">

            <h2 style="color: #2c3e50;">Smart Lost & Found System</h2>

            <p>Hello,</p>

            <p>%s</p>

            <div style="margin: 20px 0; padding: 15px; background-color: #eef2f7; border-radius: 5px;">
                <p><strong>Item:</strong> %s</p>
                <p><strong>Contact Phone:</strong> %s</p>
            </div>

            <p>Please log in to the system for more details.</p>

            <hr>

            <p style="font-size: 12px; color: gray;">
                This is an automated email. Please do not reply.
            </p>

        </div>

    </body>
    </html>
    """.formatted(messageBody, itemName, phoneNumber);
    }

    @Override
    public void sendHtmlEmail(String to, String subject, String title, String body) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);

            String html = """
                <html>
                <body style="font-family: Arial; background:#f4f6f8; padding:20px;">
                  <div style="max-width:600px; margin:auto; background:white; padding:20px; border-radius:8px;">
                    <h2 style="color:#2c3e50;">%s</h2>
                    <p>%s</p>
                    <hr>
                    <p style="font-size:12px; color:gray;">
                      Smart Lost & Found System
                    </p>
                  </div>
                </body>
                </html>
                """.formatted(title, body);

            helper.setText(html, true);
            javaMailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }

    @Override
    public void sendEmail(String to, String subject, String body, String itemName) {
        log.info("Sending email to " + to);
        try {
            MimeMessage message = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);

            String htmlContent = buildHtmlTemplate(itemName, body, "3184970237");

            helper.setText(htmlContent, true); // true = HTML

            javaMailSender.send(message);

        } catch (Exception e) {
            log.error("Error sending email", e);
        }
    }

    @Override
    public void sendResetPasswordEmail(String to, String resetLink) {
        log.info("Sending email to " + to);
        try {
            String subject = "Reset Your Password";

            String body = """
                    <h3>Password Reset Request</h3>
                    <p>Click the link below to reset your password:</p>
                    <a href="%s">Reset Password</a>
                    <p>This link will expire in 30 minutes.</p>
                    """.formatted(resetLink);

            MimeMessage message = javaMailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); // true = HTML

            javaMailSender.send(message);
        } catch (Exception e) {
            log.error("Error sending email", e);
        }
    }
}
