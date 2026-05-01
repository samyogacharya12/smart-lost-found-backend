package college.smart_lost_found_backend.service;

import college.smart_lost_found_backend.dto.CategoryDto;
import college.smart_lost_found_backend.dto.RestResponse;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    RestResponse save(CategoryDto categoryDto);

    CategoryDto findById(Long categoryId);

    List<CategoryDto> findAll();

    RestResponse update(CategoryDto categoryDto);
}
