package br.com.ecommerce.v1.category;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

public class CategoryUpdateRequest {

    @NotBlank
    @JsonProperty
    private String name;

    public CategoryUpdateRequest(String name) {
        this.name = name;
    }

    @Deprecated
    public CategoryUpdateRequest() {
    }

    public String getName() {
        return this.name;
    }
}
