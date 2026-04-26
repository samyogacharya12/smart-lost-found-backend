package college.smart_lost_found_backend.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {

    private Long userId;
    private String fullName;
    private String email;
    private String password;
    private String phoneNumber;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
