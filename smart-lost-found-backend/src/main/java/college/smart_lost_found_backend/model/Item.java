package college.smart_lost_found_backend.model;

import college.smart_lost_found_backend.enumconstant.ItemStatus;
import college.smart_lost_found_backend.enumconstant.ItemType;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Item implements Serializable {

    private Long itemId;
    private Long userId;
    private String userName;
    private Long imageId;
    private Long categoryId;
    private Long locationId;
    private String locationName;
    private String title;
    private String description;

    private ItemType itemType; // LOST or FOUND
    private ItemStatus status;   // OPEN, CLAIMED, RETURNED, CLOSED
    private LocalDate dateLostOrFound;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
