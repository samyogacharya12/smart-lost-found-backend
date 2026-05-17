package college.smart_lost_found_backend.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ClaimDto implements Serializable {

    private Long claimId;
    private Long itemId;
    private String itemName;
    private Long imageId;
    private Long userId;
    private String userName;
    private String claimMessage;
    private String status;
    private String imageUrl;
}
