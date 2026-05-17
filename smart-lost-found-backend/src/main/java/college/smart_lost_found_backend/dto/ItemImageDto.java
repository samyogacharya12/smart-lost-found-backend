package college.smart_lost_found_backend.dto;

import lombok.Data;

@Data
public class ItemImageDto {

    private Long id;

    private Long itemId;

    private Long claimId;

    private String path;

}
