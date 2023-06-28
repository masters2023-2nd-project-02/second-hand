package com.secondhand.web.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.secondhand.domain.categorie.Category;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.Entity;

@Getter
public class CategoryResponse {

    @JsonProperty("categoryId")
    private final Long categoryId;

    @JsonProperty("name")
    private final String name;

    @JsonProperty("imgUrl")
    private final String imgUrl;

    @JsonProperty("placeholder")
    private final String placeholder;

    @Builder
    public CategoryResponse(Long categoryId, String name, String imgUrl, String placeholder) {
        this.categoryId = categoryId;
        this.name = name;
        this.imgUrl = imgUrl;
        this.placeholder = placeholder;
    }
}