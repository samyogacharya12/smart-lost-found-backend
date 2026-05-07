package college.smart_lost_found_backend.api;

import college.smart_lost_found_backend.dto.CategoryDto;
import college.smart_lost_found_backend.dto.RestResponse;
import college.smart_lost_found_backend.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class CategoryController {


    @Autowired
    CategoryService categoryService;


    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/category")
    public ResponseEntity<RestResponse> save(@RequestBody CategoryDto categoryDto) {
        log.info("CategoryController save categoryDto ");
        return new ResponseEntity<>(categoryService.save(categoryDto), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/category")
    public ResponseEntity<RestResponse> update(@RequestBody CategoryDto categoryDto) {
        log.info("CategoryController update categoryDto ");
        return new ResponseEntity<>(categoryService.update(categoryDto), HttpStatus.OK);
    }



    @GetMapping("/category")
    public ResponseEntity<List<CategoryDto>> getCategories() {
        log.info("fetching categories");
        return new ResponseEntity<>(categoryService.findAll(), HttpStatus.OK);
    }


    @PostMapping("/categoryById")
    public ResponseEntity<CategoryDto> getCategoryById(@RequestBody CategoryDto categoryDto) {
        log.info("fetching category by id");
        return new ResponseEntity<>(categoryService.findById(categoryDto.getCategoryId()),
                HttpStatus.OK);
    }





}
