package com.asobal.hackathon.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("score")
@Data
@Builder
public class FeedScore {
    @Id
    private String id;

    private String objectId;
    private String type;
    private String userId;
    private long timeSpent;
    private LocalDateTime date;
}
