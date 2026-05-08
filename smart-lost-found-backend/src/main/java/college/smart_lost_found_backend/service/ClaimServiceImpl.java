package college.smart_lost_found_backend.service;

import college.smart_lost_found_backend.dao.ClaimDao;
import college.smart_lost_found_backend.dao.ItemDao;
import college.smart_lost_found_backend.dao.UserDao;
import college.smart_lost_found_backend.dto.ClaimDto;
import college.smart_lost_found_backend.dto.UserDto;
import college.smart_lost_found_backend.enumconstant.ClaimStatus;
import college.smart_lost_found_backend.enumconstant.ItemStatus;
import college.smart_lost_found_backend.exceptions.Invalid;
import college.smart_lost_found_backend.mapper.ClaimMapper;
import college.smart_lost_found_backend.model.Claim;
import college.smart_lost_found_backend.model.Item;
import college.smart_lost_found_backend.model.User;
import college.smart_lost_found_backend.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class ClaimServiceImpl implements ClaimService {


    private final ClaimDao claimDao;
    private final ItemDao itemDao;

    private final EmailService emailService;

    private final UserDao userDao;

    private final NotificationService notificationService;

    public ClaimServiceImpl(ClaimDao claimDao,
                            ItemDao itemDao,
                            EmailService emailService,
                            UserDao userDao,
                            NotificationService notificationService) {
        this.claimDao = claimDao;
        this.itemDao = itemDao;
        this.emailService = emailService;
        this.userDao = userDao;
        this.notificationService = notificationService;
    }


    @Override
    public ClaimDto save(ClaimDto claimDto) {
        log.info("ClaimServiceImpl save claimDto {}", claimDto);
        Optional<Item> item = itemDao.findById(claimDto.getItemId());
        if (item.isEmpty()) {
          throw new Invalid("Item not found", claimDto);
        }
        try {
            UserDto userDto=SecurityUtil.getCurrentUser();
            claimDto.setUserId(userDto.getId());
            Claim claim = ClaimMapper.toEntity(claimDto);
            claim.setStatus(ClaimStatus.PENDING);

            claimDao.save(claim);


            item.ifPresent(value -> notificationService.sendNotification(
                    claim.getUserId(),
                    "Claim Submitted",
                    "Your claim for" + value.getTitle() +
                            "has been submitted and is waiting for admin verification."));

            String email = userDao.findEmailByUserId(claim.getUserId());
            emailService.sendEmail(
                    email,
                    "Claim Submitted",
                    "Your claim has been submitted and is waiting for admin verification.",
                    item.get().getTitle()
            );

            // When user claims item, item becomes CLAIMED
            itemDao.updateStatus(claim.getItemId(),
                    ItemStatus.CLAIMED);

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
        try {
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
    public List<ClaimDto> findByUserId() {
        try {
            UserDto userDto= SecurityUtil.getCurrentUser();
            return claimDao.findByUserId(userDto.getId())
                    .stream()
                    .map(ClaimMapper::toDto)
                    .toList();
        } catch (Exception e) {
            log.error("ClaimServiceImpl findByUserId {}", e);
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
            Optional<Item> item = itemDao.findById(claim.getItemId());
            claimDao.updateStatus(claimId, ClaimStatus.APPROVED);
             if(item.isPresent()) {
                 notificationService.sendNotification(
                         claim.getUserId(),
                         "Claim Approved",
                         "Your claim for"+ item.get().getTitle()
                                 +"has been approved after admin verification."
                 );


                 String email = userDao.findEmailByUserId(claim.getUserId());

                 emailService.sendEmail(
                         email,
                         "Claim Approved",
                         "Your claim has been approved. The item is marked as returned.",
                               item.get().getTitle()

                 );

             }
            // Once approved, item is returned
            itemDao.updateStatus(claim.getItemId(), ItemStatus.RETURNED);

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
            Optional<Item> item = itemDao.findById(claim.getItemId());
            item.ifPresent(value -> notificationService.sendNotification(
                    claim.getUserId(),
                    "Claim Rejected",
                    "Your claim for" + value.getTitle() + "has been rejected after admin verification."
            ));

            // 4. Send EMAIL (NEW)
            String email = userDao
                    .findEmailByUserId(claim.getUserId());

            emailService.sendEmail(
                    email,
                    "Claim Rejected",
                    "Your claim for the item has been rejected. Please contact support if needed.",
                    item.get().getTitle()
            );
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
        } catch (Exception e) {
            log.error("ClaimServiceImpl deleteById {}", claimId, e);
        }
    }
}
