package college.smart_lost_found_backend.dao;

import college.smart_lost_found_backend.model.Category;
import college.smart_lost_found_backend.model.User;

import java.util.List;
import java.util.Optional;

public interface CategoryDao {

    int save(Category category);

    Optional<Category> findById(Long categoryId);

    List<Category> findAll();

    int update(Category category);

}
