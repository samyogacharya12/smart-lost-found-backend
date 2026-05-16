package college.smart_lost_found_backend.service;

public interface EmailService {

    void sendHtmlEmail(String to, String subject, String title, String body);
    void sendEmail(String to, String subject, String body, String itemName);

    void sendResetPasswordEmail(String to, String resetLink);
}
