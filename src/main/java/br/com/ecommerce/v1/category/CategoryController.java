package br.com.ecommerce.v1.category;

import net.bytebuddy.build.RepeatedAnnotationPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;

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
    public ResponseEntity<?> categoryRegistration (@Valid @RequestBody CategoryRequest categoryRequest, UriComponentsBuilder uri) {
        Category newCategory = categoryRequest.toModel();

        repository.save(newCategory);

        URI location = uri.path("/categories/{id}").buildAndExpand(newCategory.getId()).toUri();

        return ResponseEntity.created(location).build();
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
