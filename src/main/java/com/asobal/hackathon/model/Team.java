package com.asobal.hackathon.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("team")
@Data
@Builder
public class Team {
    @Id
    private String id;
    private String name;
    private String logo;
    private int score;
    private int position;
    private String tag;
}
