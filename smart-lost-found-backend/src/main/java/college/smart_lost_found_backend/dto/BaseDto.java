package college.smart_lost_found_backend.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BaseDto implements Serializable {


    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    private boolean deleted;

    private boolean status;
}
