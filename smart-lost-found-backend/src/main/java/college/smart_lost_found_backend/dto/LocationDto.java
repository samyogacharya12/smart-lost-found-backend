package college.smart_lost_found_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {

    private Long locationId;
    private String locationName;
    private String locationDescription;
    private LocalDateTime createdAt;
}
