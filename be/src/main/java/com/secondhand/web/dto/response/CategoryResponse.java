package com.secondhand.web.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CategoryResponse {

    @JsonProperty("categoryId")
    private final Long categoryId;

    @JsonProperty("name")
    private final String name;

    @JsonProperty("imgUrl")
    private final String imgUrl;

    @Builder
    public CategoryResponse(Long categoryId, String name, String imgUrl) {
        this.categoryId = categoryId;
        this.name = name;
        this.imgUrl = imgUrl;
    }
}