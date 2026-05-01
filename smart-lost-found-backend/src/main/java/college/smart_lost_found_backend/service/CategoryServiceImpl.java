package college.smart_lost_found_backend.service;

import college.smart_lost_found_backend.dao.CategoryDao;
import college.smart_lost_found_backend.dto.CategoryDto;
import college.smart_lost_found_backend.dto.RestResponse;
import college.smart_lost_found_backend.enumconstant.ResponseStatus;
import college.smart_lost_found_backend.mapper.CategoryMapper;
import college.smart_lost_found_backend.model.Category;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryDao categoryDao;

    private final CategoryMapper categoryMapper;


    public CategoryServiceImpl(CategoryDao categoryDao,
                               CategoryMapper categoryMapper) {
        this.categoryDao = categoryDao;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public RestResponse save(CategoryDto categoryDto) {
        log.info("save categoryDto:{}");
        RestResponse response = new RestResponse();
        try {
            Category category = categoryMapper.toEntity(categoryDto);
            int value= categoryDao.save(category);
            if (value > 0) {
                response.setMessage("Category is successfully saved "+category.getCategoryId());
                response.setResponseStatus(ResponseStatus.SUCCESS);
                return response;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return RestResponse
                .builder()
                .status(ResponseStatus.INTERNAL_SERVER_ERROR.getValue())
                .build();
    }

    @Override
    public Optional<CategoryDto> findById(Long categoryId) {
        log.info("findById:{}");
        try {
            return categoryDao
                    .findById(categoryId)
                    .map(CategoryMapper::toDto);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<CategoryDto> findAll() {
        log.info("findAll");
        try {
            return categoryDao
                    .findAll()
                    .stream()
                    .map(CategoryMapper::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public int update(CategoryDto categoryDto) {
        log.info("update categoryDto:{}");
        try {
            Category category = categoryMapper.toEntity(categoryDto);
            return categoryDao.update(category);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return 0;
    }
}
