package br.com.ecommerce.v1.address;

import javax.validation.constraints.NotBlank;

public class AddressResponse {

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

    public AddressResponse(Address address) {
        this.street = address.getStreet();
        this.neighborhood = address.getNeighborhood();
        this.number = address.getNumber();
        this.complement = address.getComplement();
        this.cep = address.getCep();
        this.city = address.getCity();
        this.state = address.getState();
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
