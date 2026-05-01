package college.smart_lost_found_backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryDto {

    private Long categoryId;

    private String categoryName;

    private String description;

    private LocalDateTime createdDate;
}
