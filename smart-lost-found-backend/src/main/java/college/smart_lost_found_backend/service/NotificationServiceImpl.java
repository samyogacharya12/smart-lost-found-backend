package college.smart_lost_found_backend.service;

import college.smart_lost_found_backend.dao.NotificationDao;
import college.smart_lost_found_backend.dto.NotificationDto;
import college.smart_lost_found_backend.mapper.NotificationMapper;
import college.smart_lost_found_backend.model.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
public class NotificationServiceImpl implements NotificationService {


    private final NotificationDao notificationDao;


    public NotificationServiceImpl(NotificationDao notificationDao) {
        this.notificationDao = notificationDao;
    }

    @Override
    public NotificationDto save(NotificationDto notificationDto) {
        log.info("Saving notification for user id: {}", notificationDto.getUserId());
        try {
            Notification notification = NotificationMapper.toEntity(notificationDto);
            notification.setRead(false);

            notificationDao.save(notification);

            return NotificationMapper.toDto(notification);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public void sendNotification(Long userId, String title, String message) {
        log.info("Sending notification to user id: {}", userId);
        try {
            Notification notification = new Notification();
            notification.setUserId(userId);
            notification.setTitle(title);
            notification.setMessage(message);
            notification.setRead(false);

            notificationDao.save(notification);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    @Override
    public NotificationDto findById(Long notificationId) {
        log.info("Finding notification by id: {}", notificationId);
        try {
            return notificationDao.findById(notificationId)
                    .map(NotificationMapper::toDto)
                    .orElseThrow(() -> new RuntimeException("Notification not found"));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new NotificationDto();
    }

    @Override
    public List<NotificationDto> findByUserId(Long userId) {
        log.info("Finding notifications by user id: {}", userId);
        try {
            return notificationDao.findByUserId(userId)
                    .stream()
                    .map(NotificationMapper::toDto)
                    .toList();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public List<NotificationDto> findUnreadByUserId(Long userId) {
        log.info("Finding unread notifications by user id: {}", userId);
        try {
            return notificationDao.findUnreadByUserId(userId)
                    .stream()
                    .map(NotificationMapper::toDto)
                    .toList();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public void markAsRead(Long notificationId) {
        log.info("Marking notification as read by user id: {}", notificationId);
        try {
            notificationDao.markAsRead(notificationId);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void markAllAsRead(Long userId) {
        log.info("Marking all notifications as read by user id: {}", userId);
        try {
            notificationDao.markAllAsRead(userId);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void deleteById(Long notificationId) {
        log.info("Deleting notification by id: {}", notificationId);
        try {
            notificationDao.deleteById(notificationId);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
