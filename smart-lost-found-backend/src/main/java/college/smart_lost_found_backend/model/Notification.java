package college.smart_lost_found_backend.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Notification implements Serializable {

    private Long id;

    private Long userId;


    private String title;

    private String message;

    private boolean read=false;

    private LocalDateTime created;

}
