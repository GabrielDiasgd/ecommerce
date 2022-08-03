package br.com.ecommerce.v1.category;

import net.bytebuddy.build.RepeatedAnnotationPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
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
    @Transactional
    public ResponseEntity<?> categoryRegistration (@Valid @RequestBody CategoryRequest categoryRequest) {
        Category newCategory = categoryRequest.toModel();

        repository.save(newCategory);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> categoryUpdating (@PathVariable Long id, @Valid @RequestBody CategoryUpdateRequest request) {
        Category category = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        category.update(request);
        repository.save(category);

        return ResponseEntity.ok().build();
    }
}
