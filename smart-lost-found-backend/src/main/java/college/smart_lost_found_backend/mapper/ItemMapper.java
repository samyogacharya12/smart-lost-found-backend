package college.smart_lost_found_backend.mapper;

import college.smart_lost_found_backend.dto.ItemDto;
import college.smart_lost_found_backend.enumconstant.ItemStatus;
import college.smart_lost_found_backend.enumconstant.ItemType;
import college.smart_lost_found_backend.model.Item;
import org.springframework.stereotype.Service;

@Service
public class ItemMapper {

    public static Item toEntity(ItemDto dto) {
        if (dto == null) return null;

        Item item = new Item();
        item.setItemId(dto.getItemId());
        item.setUserId(dto.getUserId());
        item.setCategoryId(dto.getCategoryId());
        item.setLocationId(dto.getLocationId());
        item.setTitle(dto.getTitle());
        item.setDescription(dto.getDescription());
        item.setItemType(Enum.valueOf(ItemType.class,
                dto.getItemType()));
        item.setStatus(Enum.valueOf(ItemStatus.class, dto.getStatus()));
        item.setDateLostOrFound(dto.getDateLostOrFound());

        return item;
    }

    public static ItemDto toDto(Item item) {
        if (item == null) return null;

        ItemDto dto = new ItemDto();
        dto.setItemId(item.getItemId());
        dto.setUserId(item.getUserId());
        dto.setCategoryId(item.getCategoryId());
        dto.setLocationId(item.getLocationId());
        dto.setTitle(item.getTitle());
        dto.setDescription(item.getDescription());
        dto.setItemType(item.getItemType().toString());
        dto.setStatus(item.getStatus().toString());
        dto.setDateLostOrFound(item.getDateLostOrFound());

        return dto;
    }
}
