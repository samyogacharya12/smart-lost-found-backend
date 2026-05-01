package college.smart_lost_found_backend.model;

import college.smart_lost_found_backend.enumconstant.ClaimStatus;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Claim implements Serializable {

    private Long claimId;
    private Long itemId;
    private Long userId;
    private String claimMessage;
    private ClaimStatus status;
    private LocalDateTime claimedAt;
    private LocalDateTime updatedAt;
}
