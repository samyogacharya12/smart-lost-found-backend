package college.smart_lost_found_backend.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ItemImage {

    private Long id;
    private Long itemId;
    private String fileType;
    private String path;
    private LocalDateTime uploadedAt;
}
