package br.com.ecommerce.v1.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private final CategoryRepository repository;

    public CategoryController(CategoryRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<?> categoryRegistration (@Valid @RequestBody CategoryRequest categoryRequest) {
        Category newCategory = categoryRequest.toModel();

        repository.save(newCategory);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
