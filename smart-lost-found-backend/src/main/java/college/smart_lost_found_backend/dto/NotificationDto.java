package college.smart_lost_found_backend.dto;

import lombok.Data;

@Data
public class NotificationDto {

    private Long id;
    private Long userId;
    private String title;
    private String message;
    private Boolean isRead;
}
