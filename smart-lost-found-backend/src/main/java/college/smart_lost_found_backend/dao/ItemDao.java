package college.smart_lost_found_backend.dao;

import college.smart_lost_found_backend.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemDao {

    int save(Item item);

    Optional<Item> findById(Long itemId);

    List<Item> findAll();

    List<Item> findByUserId(Long userId);

    List<Item> findByItemType(String itemType);

    int update(Item item);

    int updateStatus(Long itemId, String status);

    int deleteById(Long itemId);
}
