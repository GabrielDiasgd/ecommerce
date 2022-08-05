package br.com.ecommerce.v1.client;

import br.com.ecommerce.v1.address.Address;

import javax.persistence.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String cpf;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDateTime createdIn = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedIn = LocalDateTime.now();

    @OneToMany(mappedBy = "client", cascade = {CascadeType.REMOVE, CascadeType.MERGE})
    private List<Address> addresses = new ArrayList<>();

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

    public List<Address> getAddresses() {
        return addresses;
    }

    public Long getId() {
        return this.id;
    }
}
