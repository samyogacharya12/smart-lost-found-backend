package college.smart_lost_found_backend.service;

import college.smart_lost_found_backend.dao.ItemDao;
import college.smart_lost_found_backend.dto.ItemDto;
import college.smart_lost_found_backend.enumconstant.ItemStatus;
import college.smart_lost_found_backend.mapper.ItemMapper;
import college.smart_lost_found_backend.model.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemDao itemDao;


    public ItemServiceImpl(ItemDao itemDao) {
        this.itemDao = itemDao;
    }

    @Override
    public ItemDto save(ItemDto itemDto) {
        log.info("Saving item");
        try {
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


    @Override
    public List<ItemDto> findAll() {
        log.info("Fetching all items");
        try {
            return itemDao.findAll()
                    .stream()
                    .map(ItemMapper::toDto)
                    .toList();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public List<ItemDto> findByUserId(Long userId) {
        log.info("Fetching items by user id: {}", userId);
        try {
            return itemDao.findByUserId(userId)
                    .stream()
                    .map(ItemMapper::toDto)
                    .toList();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public List<ItemDto> findByItemType(String itemType) {
        log.info("Fetching items by type: {}", itemType);
        try {
            return itemDao.findByItemType(itemType)
                    .stream()
                    .map(ItemMapper::toDto)
                    .toList();
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

            itemDto.setItemId(itemId);

            Item item = ItemMapper.toEntity(itemDto);
            itemDao.update(item);

            return ItemMapper.toDto(item);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public ItemDto updateStatus(Long itemId, String status) {
        log.info("Updating item status. Item id: {}, status: {}", itemId, status);
        try {
            itemDao.findById(itemId)
                    .orElseThrow(() -> new RuntimeException("Item not found"));

            int response = itemDao.updateStatus(itemId,
                    ItemStatus.valueOf(status));
            if (response == 1) {
                Optional<Item> item = itemDao.findById(itemId);
                if (item.isPresent()) {
                    return ItemMapper.toDto(item.get());
                }
            }
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
