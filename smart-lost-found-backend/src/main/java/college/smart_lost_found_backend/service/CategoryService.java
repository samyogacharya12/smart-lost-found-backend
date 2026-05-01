package college.smart_lost_found_backend.service;

import college.smart_lost_found_backend.dto.CategoryDto;
import college.smart_lost_found_backend.dto.RestResponse;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    RestResponse save(CategoryDto categoryDto);

    Optional<CategoryDto> findById(Long categoryId);

    List<CategoryDto> findAll();

    int update(CategoryDto categoryDto);
}
