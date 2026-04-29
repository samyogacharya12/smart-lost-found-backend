package college.smart_lost_found_backend.dto;

import college.smart_lost_found_backend.enumconstant.ResponseStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestResponse {

    private String message;
    private String status;
    private Object detail;
    private ResponseStatus responseStatus;
}
