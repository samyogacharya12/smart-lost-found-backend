package college.smart_lost_found_backend.service;

import college.smart_lost_found_backend.dao.ClaimDao;
import college.smart_lost_found_backend.dao.ItemDao;
import college.smart_lost_found_backend.dto.ClaimDto;
import college.smart_lost_found_backend.enumconstant.ClaimStatus;
import college.smart_lost_found_backend.enumconstant.ItemStatus;
import college.smart_lost_found_backend.mapper.ClaimMapper;
import college.smart_lost_found_backend.model.Claim;
import jdk.jfr.Threshold;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class ClaimServiceImpl implements ClaimService {


    private final ClaimDao claimDao;
    private final ItemDao itemDao;

    public ClaimServiceImpl(ClaimDao claimDao, ItemDao itemDao) {
        this.claimDao = claimDao;
        this.itemDao = itemDao;
    }


    @Override
    public ClaimDto save(ClaimDto claimDto) {
        log.info("ClaimServiceImpl save claimDto {}", claimDto);
        try{
            Claim claim = ClaimMapper.toEntity(claimDto);
            claim.setStatus(ClaimStatus.PENDING);

            claimDao.save(claim);

            // When user claims item, item becomes CLAIMED
            itemDao.updateStatus(claim.getItemId(), ItemStatus.CLAIMED.toString());

            return ClaimMapper.toDto(claim);
        } catch (Exception e) {
            log.error("ClaimServiceImpl save claimDto {}", claimDto, e);
        }
        return null;
    }

    @Override
    public ClaimDto findById(Long claimId) {
        log.info("ClaimServiceImpl findById {}", claimId);
        try {
            return claimDao.findById(claimId)
                    .map(ClaimMapper::toDto)
                    .orElseThrow(() -> new RuntimeException("Claim not found"));
        } catch (Exception e) {
            log.error("ClaimServiceImpl findById {}", claimId, e);
        }
        return null;
    }

    @Override
    public List<ClaimDto> findAll() {
        log.info("ClaimServiceImpl findAll");
        try {
            return claimDao.findAll()
                    .stream()
                    .map(ClaimMapper::toDto)
                    .toList();
        } catch (Exception e) {
            log.error("ClaimServiceImpl findAll {}", claimDao, e);
        }
        return null;
    }

    @Override
    public List<ClaimDto> findByItemId(Long itemId) {
        log.info("ClaimServiceImpl findByItemId {}", itemId);
        try{
            return claimDao.findByItemId(itemId)
                    .stream()
                    .map(ClaimMapper::toDto)
                    .toList();
        } catch (Exception e) {
                log.error("ClaimServiceImpl findByItemId {}", itemId, e);
        }
        return List.of();
    }

    @Override
    public List<ClaimDto> findByUserId(Long userId) {
        try{
            return claimDao.findByUserId(userId)
                    .stream()
                    .map(ClaimMapper::toDto)
                    .toList();
        } catch (Exception e) {
            log.error("ClaimServiceImpl findByUserId {}", userId, e);
        }
        return List.of();
    }

    @Override
    @Transactional
    public ClaimDto approveClaim(Long claimId) {
        log.info("Approving claim id: {}");
        try {
            Claim claim = claimDao.findById(claimId)
                    .orElseThrow(() -> new RuntimeException("Claim not found"));

            claimDao.updateStatus(claimId, ClaimStatus.APPROVED);

            // Reject other pending claims for the same item
            claimDao.rejectOtherClaims(claim.getItemId(), claimId);

            // Once approved, item is returned
            itemDao.updateStatus(claim.getItemId(), "RETURNED");
            claim = claimDao.findById(claimId)
                    .orElseThrow(() -> new RuntimeException("Claim not found"));
            return ClaimMapper.toDto(claim);
        } catch (Exception e) {
            log.error("Approving claim {}", claimId, e);
        }
        return null;
    }

    @Override
    @Transactional
    public ClaimDto rejectClaim(Long claimId) {
          log.info("ClaimServiceImpl rejectClaim {}", claimId);
          try {
            claimDao.updateStatus(claimId, ClaimStatus.REJECTED);
             Claim claim = claimDao.findById(claimId)
                      .orElseThrow(() -> new RuntimeException("Claim not found"));
              return ClaimMapper.toDto(claim);
          } catch (Exception e) {
              log.error("ClaimServiceImpl rejectClaim {}", claimId, e);
          }
          return null;
    }

    @Override
    public void deleteById(Long claimId) {
      log.info("ClaimServiceImpl deleteById {}", claimId);
      try {
            claimDao.deleteById(claimId);
      }catch (Exception e) {
          log.error("ClaimServiceImpl deleteById {}", claimId, e);
      }
    }
}
