package college.smart_lost_found_backend.dao;

import college.smart_lost_found_backend.model.ItemImage;

import java.util.List;

public interface ItemImageDao {

    int save(Long itemId, String imageUrl);

    List<ItemImage> findByItemId(Long itemId);

    int deleteById(Long imageId);

}

