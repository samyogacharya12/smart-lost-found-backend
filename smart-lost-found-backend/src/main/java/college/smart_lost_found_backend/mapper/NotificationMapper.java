package college.smart_lost_found_backend.mapper;

import college.smart_lost_found_backend.dto.NotificationDto;
import college.smart_lost_found_backend.model.Notification;

public class NotificationMapper {


    public static Notification toEntity(NotificationDto dto) {
        if (dto == null) return null;

        Notification notification = new Notification();
        notification.setId(dto.getId());
        notification.setUserId(dto.getUserId());
        notification.setTitle(dto.getTitle());
        notification.setMessage(dto.getMessage());
        notification.setRead(dto.getIsRead() == null ? false : dto.getIsRead());

        return notification;
    }

    public static NotificationDto toDto(Notification notification) {
        if (notification == null) return null;

        NotificationDto dto = new NotificationDto();
        dto.setId(notification.getId());
        dto.setUserId(notification.getUserId());
        dto.setTitle(notification.getTitle());
        dto.setMessage(notification.getMessage());
        dto.setIsRead(notification.isRead());

        return dto;
    }
}
