package college.smart_lost_found_backend.dao;

import college.smart_lost_found_backend.enumconstant.ImageType;
import college.smart_lost_found_backend.model.SystemImage;

import java.util.List;
import java.util.Optional;

public interface SystemDao {

    int save(SystemImage systemImage);

    List<SystemImage> findAll();

    SystemImage findById(Long id);

    Optional<SystemImage> findByImageName(String imageName);
    List<SystemImage>  findByImageType(ImageType imageType);
    int delete(Long imageId);

}
