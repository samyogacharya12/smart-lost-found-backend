package college.smart_lost_found_backend.service;

import college.smart_lost_found_backend.dao.ClaimDao;
import college.smart_lost_found_backend.dao.ItemDao;
import college.smart_lost_found_backend.dao.ItemImageDao;
import college.smart_lost_found_backend.dao.UserDao;
import college.smart_lost_found_backend.dto.ClaimDto;
import college.smart_lost_found_backend.dto.UserDto;
import college.smart_lost_found_backend.enumconstant.ClaimStatus;
import college.smart_lost_found_backend.enumconstant.ItemStatus;
import college.smart_lost_found_backend.enumconstant.ItemType;
import college.smart_lost_found_backend.exceptions.Invalid;
import college.smart_lost_found_backend.mapper.ClaimMapper;
import college.smart_lost_found_backend.model.Claim;
import college.smart_lost_found_backend.model.Item;
import college.smart_lost_found_backend.model.ItemImage;
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

    private final ItemImageDao itemImageDao;


    public ClaimServiceImpl(ClaimDao claimDao,
                            ItemDao itemDao,
                            EmailService emailService,
                            UserDao userDao,
                            NotificationService notificationService,
                            ItemImageDao itemImageDao) {
        this.claimDao = claimDao;
        this.itemDao = itemDao;
        this.emailService = emailService;
        this.userDao = userDao;
        this.notificationService = notificationService;
        this.itemImageDao = itemImageDao;
    }


    @Override
    public ClaimDto save(ClaimDto claimDto,  UserDto userDto) {
        log.info("ClaimServiceImpl save claimDto {}", claimDto);
        Optional<Item> item = itemDao.findById(claimDto.getItemId());
        if (item.isEmpty()) {
          throw new Invalid("Item not found", claimDto);
        }
        assert userDto != null;
        if(item.get().getUserId().equals(userDto.getId())){
            throw new Invalid("You cannot claim your own item", claimDto);
        }
        if(!item.get().getItemType().equals(ItemType.FOUND)){
            throw new Invalid("ItemType not found", claimDto);
        }
        try {
            claimDto.setUserId(userDto.getId());
            Claim claim = ClaimMapper.toEntity(claimDto);
            claim.setStatus(ClaimStatus.PENDING);
            int claimId=claimDao.save(claim);
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
            claim.setClaimId((long) claimId);
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
            List<ClaimDto> claimDtos= claimDao.findAll()
                    .stream()
                    .map(ClaimMapper::toDto)
                    .toList();
            return mapToDto(claimDtos);
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

    private List<ClaimDto> mapToDto(List<ClaimDto> claimDtos) {
        claimDtos.forEach(claimDto -> {
            Optional<Item> item=itemDao.findById(claimDto.getItemId());
            Optional<ItemImage> itemImage=itemImageDao.getImageByClaimId(claimDto.getClaimId());
            itemImage.ifPresent(image -> claimDto.setImageId(image.getId()));
            item.ifPresent(item1 -> {claimDto.setItemName(item.get().getTitle());});
            Optional<User> user=userDao.findById(claimDto.getUserId());
            user.ifPresent(user1 -> {claimDto.setUserName(user1.getUsername());});
        });
        return claimDtos;
    }


        @Override
    public List<ClaimDto> findByUserId() {
        try {
            String username=SecurityUtil.getCurrentUsername();
            Optional<User> user=userDao.findByName(username);
            if (user.isPresent()) {
                List<ClaimDto> claimDtos= claimDao.findByUserId(user.get().getUserId())
                        .stream()
                        .map(ClaimMapper::toDto)
                        .toList();
                return mapToDto(claimDtos);
            }
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
                         "Your claim has been approved successfully. Please visit the Heritage Hall with valid proof of ownership to collect your item.",
                               item.get().getTitle()

                 );

             }
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
                    "Your claim for the item has been rejected. For further assistance, please contact the staff at the Heritage Hall support desk..",
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
