package college.smart_lost_found_backend.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {

    private Long userId;
    private String username;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private String role;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean emailVerified;
    private String verificationToken;
    private LocalDateTime tokenExpiry;

}
