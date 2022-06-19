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
    private LocalDateTime dateCreation = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime dateUpdate = LocalDateTime.now();

    public Category(String name) {
        this.name = name;
    }

    /**
     * Exclusive use of hibernate
     */
    @Deprecated
    public Category() {
    }

}

