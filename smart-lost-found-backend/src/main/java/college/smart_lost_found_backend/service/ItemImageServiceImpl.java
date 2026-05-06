package college.smart_lost_found_backend.service;

import college.smart_lost_found_backend.dao.ItemDao;
import college.smart_lost_found_backend.dao.ItemImageDao;
import college.smart_lost_found_backend.dto.ItemDto;
import college.smart_lost_found_backend.dto.ItemImageDto;
import college.smart_lost_found_backend.dto.UserDto;
import college.smart_lost_found_backend.enumconstant.ItemStatus;
import college.smart_lost_found_backend.mapper.ItemImageMapper;
import college.smart_lost_found_backend.mapper.ItemMapper;
import college.smart_lost_found_backend.model.Item;
import college.smart_lost_found_backend.model.ItemImage;
import college.smart_lost_found_backend.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@Transactional
public class ItemImageServiceImpl implements ItemImageService {

    private final ItemDao itemDao;

    private final ItemImageDao itemImageDao;

    private final UserService userService;


    private static final String UPLOAD_DIR = "uploads/items/";

    public ItemImageServiceImpl(ItemImageDao itemImageDao,
                                ItemDao itemDao,
                                UserService userService) {
        this.itemImageDao = itemImageDao;
        this.itemDao = itemDao;
        this.userService = userService;
    }

    @Override
    public ItemImageDto uploadImage(Long itemId, MultipartFile file) {
        log.info("uploadImage");
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }
        try {
            Path path = Paths.get(UPLOAD_DIR);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            String originalFileName = file.getOriginalFilename();
            String fileName = UUID.randomUUID() + "_" + originalFileName;

            Path filePath = Paths.get(UPLOAD_DIR + fileName);
            Files.write(filePath, file.getBytes());

//            String imageUrl = "/" + UPLOAD_DIR + fileName;
            itemImageDao.save(itemId, filePath.toAbsolutePath().toString());

            ItemImageDto dto = new ItemImageDto();
            dto.setItemId(itemId);
            dto.setPath(filePath.toAbsolutePath().toString());

            return dto;

        } catch (Exception e) {
            throw new RuntimeException("Failed to upload image: " + e.getMessage());
        }
    }

    @Override
    public List<ItemImageDto> getImagesByItemId(Long itemId) {
        log.info("getImagesByItemId");
        try {
            return itemImageDao.findByItemId(itemId)
                    .stream()
                    .map(ItemImageMapper::toDto)
                    .toList();
        } catch (Exception exception) {
            log.error("getImagesByItemId");
        }
        return List.of();
    }

    @Override
    public byte[] downloadImage(Long itemId, Long itemImageId) {
        log.info("downloadImage");
        try {
            Path filePath = null;
            Optional<ItemImage> imageDto = itemImageDao.getImageByIdAndItemId(itemImageId, itemId);
            if (imageDto.isPresent()) {
                filePath = Paths.get(imageDto.get().getPath());
            }
            return Files.readAllBytes(filePath);
        } catch (Exception exception) {
            log.error("error in download image: " + exception.getMessage());
        }
        return null;
    }

    @Override
    public void deleteImage(Long imageId) {
        itemImageDao.deleteById(imageId);
    }

    @Override
    public ItemDto saveItemWithImage(ItemDto itemDto, MultipartFile file) {
        log.info("saving item with image");
        try {
            String username = SecurityUtil.getCurrentUsername();
            Optional<UserDto> userDto = userService.findByUsername(username);
            userDto.ifPresent(dto -> itemDto.setUserId(dto.getId()));
            itemDto.setStatus(ItemStatus.OPEN.toString());
            Item item = ItemMapper.toEntity(itemDto);
            int itemId = itemDao.save(item);
            itemDto.setItemId((long) itemId);
            if (file != null && !file.isEmpty()) {
                uploadImage(Long.parseLong(String.valueOf(itemId)), file);
            }
            return itemDto;
        } catch (Exception exception) {
            log.error("error in saving item with image: " + exception.getMessage());
        }
        return null;
    }
}
