package college.smart_lost_found_backend.dao;

import college.smart_lost_found_backend.model.Notification;

import java.util.List;
import java.util.Optional;

public interface NotificationDao {

    int save(Notification notification);

    Optional<Notification> findById(Long notificationId);

    List<Notification> findByUserId(Long userId);

    List<Notification> findUnreadByUserId(Long userId);

    int markAsRead(Long notificationId);

    int markAllAsRead(Long userId);

    int deleteById(Long notificationId);
}
