package br.com.ecommerce.v1.user;

import javax.persistence.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String cpf;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime dateUpdate = LocalDateTime.now();

    /**
     * Exclusive use of hibernate
     */
    @Deprecated
    public Client() {
    }

    public Client(String name, String cpf, String email, String password) {
        this.name = name;
        this.cpf = cpf;
        this.email = email;
        this.password = password.getBytes(StandardCharsets.UTF_8).toString();
    }
}
