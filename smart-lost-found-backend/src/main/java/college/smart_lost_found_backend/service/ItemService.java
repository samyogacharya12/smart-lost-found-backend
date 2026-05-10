package college.smart_lost_found_backend.service;

import college.smart_lost_found_backend.dto.ItemDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ItemService {

    ItemDto save(ItemDto itemDto);

    ItemDto findById(Long itemId);

    List<ItemDto> findAll();

    List<ItemDto> findByUserId();

    List<ItemDto> findByItemType(String itemType);

    ItemDto update(Long itemId, ItemDto itemDto);

    ItemDto updateStatus(ItemDto itemDto);

    List<ItemDto> searchItems(String itemName, Long locationId);

    void deleteById(Long itemId);

}
