package college.smart_lost_found_backend.service;

public interface EmailService {

    void sendEmail(String to, String subject, String body);
}
