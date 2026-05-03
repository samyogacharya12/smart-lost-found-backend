package college.smart_lost_found_backend.dao;

import college.smart_lost_found_backend.model.ItemImage;

import java.util.List;
import java.util.Optional;

public interface ItemImageDao {

    int save(Long itemId, String imageUrl);


    Optional<ItemImage> getImageByIdAndItemId(Long imageId, Long itemId);

    List<ItemImage> findByItemId(Long itemId);

    int deleteById(Long imageId);

}

