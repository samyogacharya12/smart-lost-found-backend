package college.smart_lost_found_backend.dao;

import college.smart_lost_found_backend.enumconstant.ClaimStatus;
import college.smart_lost_found_backend.model.Claim;

import java.util.List;
import java.util.Optional;

public interface ClaimDao {

    int save(Claim claim);

    Optional<Claim> findById(Long claimId);

    List<Claim> findAll();

    List<Claim> findByItemId(Long itemId);

    List<Claim> findByUserId(Long userId);

    int updateStatus(Long claimId, ClaimStatus status);

    int rejectOtherClaims(Long itemId, Long approvedClaimId);

    int deleteById(Long claimId);
}
