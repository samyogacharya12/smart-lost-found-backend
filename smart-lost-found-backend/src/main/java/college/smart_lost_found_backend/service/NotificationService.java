package college.smart_lost_found_backend.service;

import college.smart_lost_found_backend.dto.NotificationDto;

import java.util.List;

public interface NotificationService {

    NotificationDto save(NotificationDto notificationDto);

    void sendNotification(Long userId, String title, String message);

    NotificationDto findById(Long notificationId);

    List<NotificationDto> findByUserId(Long userId);

    List<NotificationDto> findUnreadByUserId(Long userId);

    void markAsRead(Long notificationId);

    void markAllAsRead(Long userId);

    void deleteById(Long notificationId);
}
