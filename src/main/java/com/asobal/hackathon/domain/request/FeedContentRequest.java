package com.asobal.hackathon.domain.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class FeedContentRequest {
    private String title;
    @NotBlank
    private String description;
    private String image;
    @Pattern(regexp = "^(COMMENT|POST)$")
    private String type;
    private String linkedPostId;
}
