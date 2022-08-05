package br.com.ecommerce.v1.address;

import br.com.ecommerce.v1.client.Client;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String neighborhood;

    @Column(nullable = false)
    private String number;

    private String complement;

    @Column(nullable = false)
    private String cep;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private LocalDateTime createdIn = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedIn = LocalDateTime.now();

    @ManyToOne(optional = false)
    private Client client;


    public Address(String street, String neighborhood, String number, String complement, String cep, String city, String state, Client client) {
        this.street = street;
        this.neighborhood = neighborhood;
        this.number = number;
        this.complement = complement;
        this.cep = cep;
        this.city = city;
        this.state = state;
        this.client = client;
    }

    /**
     * @deprecated Exclusive use of hibernate
     */
    @Deprecated
    public Address() {
    }

    public String getStreet() {
        return street;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public String getNumber() {
        return number;
    }

    public String getComplement() {
        return complement;
    }

    public String getCep() {
        return cep;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public Long getId() {
        return this.id;
    }

    public boolean belongs(Client client) {
        return client.getId().equals(this.client.getId());
    }
}
