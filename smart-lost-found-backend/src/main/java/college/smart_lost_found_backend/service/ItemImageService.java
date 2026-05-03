package college.smart_lost_found_backend.service;

import college.smart_lost_found_backend.dto.ItemDto;
import college.smart_lost_found_backend.dto.ItemImageDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ItemImageService {


    ItemImageDto uploadImage(Long itemId, MultipartFile file);

    List<ItemImageDto> getImagesByItemId(Long itemId);

    byte[] downloadImage(Long itemId, Long itemImageId);

    void deleteImage(Long imageId);

    ItemDto saveItemWithImage(ItemDto itemDto, MultipartFile file);


}
