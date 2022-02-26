package com.asobal.hackathon.domain.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class FeedScoreRequest {
    @NotBlank
    private String id;
    @Pattern(regexp = "^(FEED|TEAM|PLAYER|NEWS)$")
    private String type;
    @NotNull
    private long time;
}
