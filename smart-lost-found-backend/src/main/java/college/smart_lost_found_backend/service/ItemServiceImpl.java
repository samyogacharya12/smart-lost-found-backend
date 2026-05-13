package college.smart_lost_found_backend.service;

import college.smart_lost_found_backend.dao.ItemDao;
import college.smart_lost_found_backend.dao.ItemImageDao;
import college.smart_lost_found_backend.dto.CategoryDto;
import college.smart_lost_found_backend.dto.ItemDto;
import college.smart_lost_found_backend.dto.LocationDto;
import college.smart_lost_found_backend.dto.UserDto;
import college.smart_lost_found_backend.enumconstant.ItemStatus;
import college.smart_lost_found_backend.enumconstant.ItemType;
import college.smart_lost_found_backend.mapper.ItemMapper;
import college.smart_lost_found_backend.model.Item;
import college.smart_lost_found_backend.model.ItemImage;
import college.smart_lost_found_backend.util.SecurityUtil;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemDao itemDao;

    private final ItemImageDao itemImageDao;

    private final CategoryService categoryService;

    private final LocationService locationService;

    private final UserService userService;


    private final EmailService emailService;

    private final NotificationService notificationService;

    private static  final String FOUND_REPORTED_EMAIL= """
Dear User,

Thank you for reporting a found item through the Smart Lost & Found System.

The administration team has identified a possible match between the item you reported and a lost item claim submitted by another user.

We appreciate your honesty and cooperation in helping return lost belongings to their rightful owner.

Location:  Heritage Hall

Thank you for contributing to the community.
""".replace("\n", "<br>");

    private static final String RETURNED_REPORTED_EMAIL = """
               Your item has been marked as returned successfully.
            
                                 Thank you for using the Smart Lost & Found System.
            
                                 Location: Heritage Hall
            
            """;




    public ItemServiceImpl(ItemDao itemDao,
                           CategoryService categoryService,
                           LocationService locationService,
                           UserService userService,
                           ItemImageDao itemImageDao,
                           EmailService emailService,
                           NotificationService notificationService) {
        this.itemDao = itemDao;
        this.categoryService = categoryService;
        this.locationService = locationService;
        this.userService = userService;
        this.itemImageDao = itemImageDao;
        this.emailService = emailService;
        this.notificationService = notificationService;
    }

    @Override
    public ItemDto save(ItemDto itemDto) {
        log.info("Saving item");
        try {
            String username = SecurityUtil.getCurrentUsername();
            Optional<UserDto> userDto= userService.findByUsername(username);
            userDto.ifPresent(dto -> itemDto.setUserId(dto.getId()));
            itemDto.setDateLostOrFound(LocalDate.now());
            Item item = ItemMapper.toEntity(itemDto);
            if (item.getStatus() == null) {
                item.setStatus(ItemStatus.OPEN);
            }
            itemDao.save(item);

            return ItemMapper.toDto(item);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public ItemDto findById(Long itemId) {
        log.info("Finding item by id: {}", itemId);
        try {
            return itemDao.findById(itemId)
                    .map(ItemMapper::toDto)
                    .orElseThrow(() -> new RuntimeException("Item not found"));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    private List<ItemDto> mapToDto(List<ItemDto> itemDtos) {
        itemDtos.forEach(itemDto -> {
            CategoryDto categoryDto = categoryService.findById(itemDto.getCategoryId());
            itemDto.setCategoryName(categoryDto.getCategoryName());
            LocationDto locationDto = locationService.findById(itemDto.getLocationId());
            itemDto.setLocationName(locationDto.getLocationName());
            List<ItemImage> itemImages=itemImageDao.findByItemId(itemDto.getItemId());
             if(!itemImages.isEmpty()){
                 itemDto.setImageId(itemImages.getFirst().getId());
             }
             UserDto userDto=userService.findByUserId(itemDto.getUserId());
             itemDto.setUserName(userDto.getUserName());
        });
        return itemDtos;
    }

    @Override
    public List<ItemDto> findAll() {
        log.info("Fetching all items");
        try {
            List<ItemDto> itemDtos = itemDao.findAll()
                    .stream()
                    .map(ItemMapper::toDto)
                    .toList();

            return mapToDto(itemDtos);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public List<ItemDto> findByUserId() {
        log.info("Fetching items by user id: {}");
        try {
            String username = SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getName();
            Optional<UserDto> user = userService.findByUsername(username);
            List<ItemDto> itemDtos=new ArrayList<>();
            if(user.isPresent()) {
                itemDtos=itemDao.findByUserId(user.get().getId())
                        .stream()
                        .map(ItemMapper::toDto)
                        .toList();
            }
            return mapToDto(itemDtos);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public List<ItemDto> findByItemType(String itemType) {
        log.info("Fetching items by type: {}", itemType);
        try {
            List<ItemDto> itemDtos;
            String username = SecurityUtil.getCurrentUsername();
            Optional<UserDto> user = userService.findByUsername(username);
            if (user.isPresent()) {
                itemDtos = itemDao.findByItemType(itemType)
                        .stream()
                        .map(ItemMapper::toDto)
                        .toList();
                return mapToDto(itemDtos);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public ItemDto update(Long itemId, ItemDto itemDto) {
        log.info("Updating item id: {}", itemId);
        try {
            itemDao.findById(itemId)
                    .orElseThrow(() -> new RuntimeException("Item not found"));

            itemDto.setItemId(itemDto.getItemId());

            Item item = ItemMapper.toEntity(itemDto);
            itemDao.update(item);

            return ItemMapper.toDto(item);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public ItemDto updateStatus(ItemDto itemDto) {
        log.info("Updating item status. Item id: {}, status: {}", itemDto.getItemId(), itemDto.getItemId());
        itemDao.findById(itemDto.getItemId()).orElseThrow(() -> new RuntimeException("Item not found"));
        try {

            int response = itemDao.updateStatus(itemDto.getItemId(), ItemStatus.valueOf(itemDto.getStatus()));
            if (response == 1) {
                Optional<Item> item = itemDao.findById(itemDto.getItemId());
                if (item.isPresent()) {
                    UserDto userDto = userService.findByUserId(item.get().getUserId());
                    item.get().setUserName(userDto.getUserName());
                    if (item.get().getItemType().equals(ItemType.LOST) && item.get().getStatus().equals(ItemStatus.MATCHED)) {
                        notificationService.sendNotification(userDto.getId(), item.get().getTitle(), "Your item request has been matched successfully. Please visit Heritage Hall with valid proof of ownership to collect your item.");

                        emailService.sendEmail(userDto.getEmail(), "Item Matched", "Your item request has been matched successfully. Please visit Heritage Hall with valid proof of ownership to collect your item.", item.get().getTitle()

                        );
                    } else if (item.get().getItemType().equals(ItemType.FOUND) && item.get().getStatus().equals(ItemStatus.MATCHED)) {
                        notificationService.sendNotification(userDto.getId(), item.get().getTitle(), FOUND_REPORTED_EMAIL);

                        emailService.sendEmail(userDto.getEmail(), "Found Item Match Confirmation", FOUND_REPORTED_EMAIL, item.get().getTitle()

                        );
                    } else if (item.get().getStatus().equals(ItemStatus.RETURNED)) {

                        notificationService.sendNotification(userDto.getId(), item.get().getTitle(), RETURNED_REPORTED_EMAIL);

                        emailService.sendEmail(userDto.getEmail(), "Item Return Confirmation", RETURNED_REPORTED_EMAIL, item.get().getTitle());
                    }


                    return ItemMapper.toDto(item.get());
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override

    public List<ItemDto> searchItems(String itemName, Long locationId, String itemType) {
        log.info("searching items by name: {}, locationId: {}", itemName, locationId);
        try {
            if(StringUtils.isBlank(itemType)){
                itemType=null;
            }

            if(StringUtils.isBlank(itemName)){
                itemName=null;
            }
            if(Objects.nonNull(locationId) && StringUtils.isBlank(locationId.toString())){
                locationId=null;
            }
                return itemDao.searchItems(itemName, locationId, itemType)
                        .stream()
                        .map(ItemMapper::toDto)
                        .toList();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public void deleteById(Long itemId) {
        log.info("Deleting item id: {}", itemId);
        try {
            itemDao.deleteById(itemId);
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }
    }
}
