package br.com.ecommerce.v1.address;

import br.com.ecommerce.v1.client.Client;

import javax.validation.constraints.NotBlank;

public class AddressRequest {

    @NotBlank
    private String street;

    @NotBlank
    private String neighborhood;

    @NotBlank
    private String number;

    private String complement;

    @NotBlank
    private String cep;

    @NotBlank
    private String city;

    @NotBlank
    private String state;

    public Address toModel(Client client) {
        return new Address(street, neighborhood, number, complement, cep, city, state, client);
    }

    public AddressRequest(String street, String neighborhood, String number, String complement, String cep, String city, String state) {
        this.street = street;
        this.neighborhood = neighborhood;
        this.number = number;
        this.complement = complement;
        this.cep = cep;
        this.city = city;
        this.state = state;
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
}
