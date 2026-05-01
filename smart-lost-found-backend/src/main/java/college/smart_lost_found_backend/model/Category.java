package college.smart_lost_found_backend.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Category {

    private Long categoryId;

    private String categoryName;

    private String description;

    private LocalDateTime createdDate;
}
