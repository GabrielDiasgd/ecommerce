package br.com.ecommerce.v1.category;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

public class CategoryRequest {

    @NotBlank
    @JsonProperty
    private String name;

    public CategoryRequest(String name) {
        this.name = name;
    }

    @Deprecated
    public CategoryRequest() {
    }

    public Category toModel() {
        return new Category(this.name);
    }
}
