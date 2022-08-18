package br.com.ecommerce.v1.category;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDateTime createdIn = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedIn = LocalDateTime.now();

    public Category(String name) {
        this.name = name;
    }

    /**
     * Exclusive use of hibernate
     */
    @Deprecated
    public Category() {
    }

    public void update(CategoryUpdateRequest request) {
        this.name = request.getName();
        this.updatedIn = LocalDateTime.now();
    }
    public Long getId() {
        return this.id;
    }

    public String getName() {
        return name;
    }
}

