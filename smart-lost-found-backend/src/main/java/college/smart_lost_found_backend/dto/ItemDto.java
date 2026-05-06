package college.smart_lost_found_backend.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class ItemDto implements Serializable {

    private Long itemId;
    private Long userId;
    private Long categoryId;
    private String categoryName;
    private Long locationId;
    private String locationName;
    private String title;
    private String description;
    private String itemType;
    private String status;
    private LocalDate dateLostOrFound;
}
