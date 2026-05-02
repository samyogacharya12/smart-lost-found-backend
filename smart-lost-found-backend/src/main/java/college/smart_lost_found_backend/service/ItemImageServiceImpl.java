package college.smart_lost_found_backend.service;

import college.smart_lost_found_backend.dao.ItemImageDao;
import college.smart_lost_found_backend.dto.ItemImageDto;
import college.smart_lost_found_backend.mapper.ItemImageMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ItemImageServiceImpl implements ItemImageService {


    private final ItemImageDao itemImageDao;

    private static final String UPLOAD_DIR = "uploads/items/";

    public ItemImageServiceImpl(ItemImageDao itemImageDao) {
        this.itemImageDao = itemImageDao;
    }

    @Override
    public ItemImageDto uploadImage(Long itemId, MultipartFile file) {
        log.info("uploadImage");
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }
        try {
            Path path = Paths.get(UPLOAD_DIR);
            if(!Files.exists(path)) {
                Files.createDirectories(path);
            }
            String originalFileName = file.getOriginalFilename();
            String fileName = UUID.randomUUID() + "_" + originalFileName;

            Path filePath = Paths.get(UPLOAD_DIR + fileName);
            Files.write(filePath, file.getBytes());

            String imageUrl = "/" + UPLOAD_DIR + fileName;

            itemImageDao.save(itemId, imageUrl);

            ItemImageDto dto = new ItemImageDto();
            dto.setItemId(itemId);
            dto.setImageUrl(imageUrl);

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
    public void deleteImage(Long imageId) {
        itemImageDao.deleteById(imageId);
    }
}
