package college.smart_lost_found_backend.mapper;

import college.smart_lost_found_backend.dto.ClaimDto;
import college.smart_lost_found_backend.enumconstant.ClaimStatus;
import college.smart_lost_found_backend.model.Claim;

public class ClaimMapper {

    public static Claim toEntity(ClaimDto dto) {
        if (dto == null) return null;

        Claim claim = new Claim();
        claim.setClaimId(dto.getClaimId());
        claim.setItemId(dto.getItemId());
        claim.setUserId(dto.getUserId());
        claim.setClaimMessage(dto.getClaimMessage());
        claim.setStatus(Enum.valueOf(ClaimStatus.class,dto.getStatus()));
        return claim;
    }

    public static ClaimDto toDto(Claim claim) {
        if (claim == null) return null;

        ClaimDto dto = new ClaimDto();
        dto.setClaimId(claim.getClaimId());
        dto.setItemId(claim.getItemId());
        dto.setUserId(claim.getUserId());
        dto.setClaimMessage(claim.getClaimMessage());
        dto.setStatus(claim.getStatus().toString());

        return dto;
    }
}
