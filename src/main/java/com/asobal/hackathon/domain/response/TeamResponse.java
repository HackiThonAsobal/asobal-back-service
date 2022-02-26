package com.asobal.hackathon.domain.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeamResponse {
    private String id;
    private String name;
    private String logo;
    private int score;
    private int position;
}
