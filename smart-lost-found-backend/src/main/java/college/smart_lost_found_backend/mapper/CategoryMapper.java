package college.smart_lost_found_backend.mapper;

import college.smart_lost_found_backend.dto.CategoryDto;
import college.smart_lost_found_backend.model.Category;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class CategoryMapper {


    public static CategoryDto toDto(Category category) {
        if (category == null) {
            return new CategoryDto();
        }
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setCategoryId(category.getCategoryId());
        categoryDto.setCategoryName(category.getCategoryName());
        categoryDto.setDescription(category.getDescription());
        return categoryDto;
    }

    public static Category toEntity(CategoryDto dto) {
        if (dto == null) {
            return new Category();
        }

        Category category = new Category();
        category.setCategoryId(dto.getCategoryId());
        category.setCategoryName(dto.getCategoryName());
        category.setDescription(dto.getDescription());
        category.setCreatedDate(LocalDateTime.now());
        return category;
    }


}
