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

    private String buildHtmlTemplate(String itemName, String messageBody) {

        return """
        <html>
        <body style="font-family: Arial, sans-serif; background-color: #f4f6f8; padding: 20px;">
            
            <div style="max-width: 600px; margin: auto; background: white; padding: 20px; border-radius: 10px;">
                
                <h2 style="color: #2c3e50;">Smart Lost & Found System</h2>
                
                <p>Hello,</p>
                
                <p>%s</p>
                
                <div style="margin: 20px 0; padding: 15px; background-color: #eef2f7; border-radius: 5px;">
                    <strong>Item:</strong> %s
                </div>
                
                <p>Please log in to the system for more details.</p>
                
                <hr>
                
                <p style="font-size: 12px; color: gray;">
                    This is an automated email. Please do not reply.
                </p>
                
            </div>
            
        </body>
        </html>
        """.formatted(messageBody, itemName);
    }

    @Override
    public void sendEmail(String to, String subject, String body, String itemName) {
        log.info("Sending email to " + to);
        try {
            MimeMessage message = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);

            String htmlContent = buildHtmlTemplate(itemName, body);

            helper.setText(htmlContent, true); // true = HTML

            javaMailSender.send(message);

        } catch (Exception e) {
            log.error("Error sending email", e);
        }
    }
}
