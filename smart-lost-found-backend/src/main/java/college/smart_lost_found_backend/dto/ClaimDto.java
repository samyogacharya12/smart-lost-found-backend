package college.smart_lost_found_backend.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ClaimDto implements Serializable {

    private Long claimId;
    private Long itemId;
    private Long userId;
    private String claimMessage;
    private String status;
}
