package college.smart_lost_found_backend.mapper;

import college.smart_lost_found_backend.dto.ItemImageDto;
import college.smart_lost_found_backend.model.ItemImage;
import org.springframework.stereotype.Service;

@Service
public class ItemImageMapper {

    public static ItemImageDto toDto(ItemImage image) {
        if (image == null) return null;

        ItemImageDto dto = new ItemImageDto();
        dto.setId(image.getId());
        dto.setItemId(image.getItemId());
        dto.setPath(image.getPath());
        return dto;
    }

}
