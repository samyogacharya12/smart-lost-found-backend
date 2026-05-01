package college.smart_lost_found_backend.service;

import college.smart_lost_found_backend.dto.ClaimDto;

import java.util.List;

public interface ClaimService {

    ClaimDto save(ClaimDto claimDto);

    ClaimDto findById(Long claimId);

    List<ClaimDto> findAll();

    List<ClaimDto> findByItemId(Long itemId);

    List<ClaimDto> findByUserId(Long userId);

    ClaimDto approveClaim(Long claimId);

    ClaimDto rejectClaim(Long claimId);

    void deleteById(Long claimId);
}
