package college.smart_lost_found_backend.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ItemImage {

    private Long id;
    private Long itemId;
    private String imageUrl;
    private LocalDateTime uploadedAt;
}
