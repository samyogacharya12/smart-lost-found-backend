package college.smart_lost_found_backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location implements Serializable {

    private Long locationId;
    private String locationName;
    private String locationDescription;
    private LocalDateTime createdAt;

}
