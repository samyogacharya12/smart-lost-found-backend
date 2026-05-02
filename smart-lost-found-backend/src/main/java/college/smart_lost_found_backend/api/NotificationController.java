package college.smart_lost_found_backend.api;

import college.smart_lost_found_backend.dto.NotificationDto;
import college.smart_lost_found_backend.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<NotificationDto> save(@RequestBody NotificationDto notificationDto) {
        return ResponseEntity.ok(notificationService.save(notificationDto));
    }

    @GetMapping("/{notificationId}")
    public ResponseEntity<NotificationDto> findById(@PathVariable Long notificationId) {
        return ResponseEntity.ok(notificationService.findById(notificationId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationDto>> findByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.findByUserId(userId));
    }

    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<List<NotificationDto>> findUnreadByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.findUnreadByUserId(userId));
    }

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<String> markAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok("Notification marked as read");
    }

    @PatchMapping("/user/{userId}/read-all")
    public ResponseEntity<String> markAllAsRead(@PathVariable Long userId) {
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok("All notifications marked as read");
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<String> deleteById(@PathVariable Long notificationId) {
        notificationService.deleteById(notificationId);
        return ResponseEntity.ok("Notification deleted successfully");
    }

}
