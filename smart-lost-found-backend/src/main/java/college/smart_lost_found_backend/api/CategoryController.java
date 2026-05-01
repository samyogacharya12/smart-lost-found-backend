package college.smart_lost_found_backend.api;

import college.smart_lost_found_backend.dto.CategoryDto;
import college.smart_lost_found_backend.dto.RestResponse;
import college.smart_lost_found_backend.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Slf4j
public class CategoryController {


    @Autowired
    CategoryService categoryService;


    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @PostMapping("/category")
    public ResponseEntity<RestResponse> save(@RequestBody CategoryDto categoryDto) {
        log.info("CategoryController save userDto ");
        return new ResponseEntity<>(categoryService.save(categoryDto), HttpStatus.OK);
    }



}
