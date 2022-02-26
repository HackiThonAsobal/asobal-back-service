package com.asobal.hackathon.model;

import com.asobal.hackathon.domain.response.CommentResponse;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Document("feed")
@Data
@Builder
public class Feed {
    @Id
    private String id;
    private String tittle;
    private String description;
    private String image;
    private String type;
    private String teamOneImage;
    private String teamTwoImage;
    private List<CommentResponse> comments;
    private LocalDate publicationDaTe;
    private LocalDate matchDate;
    private List<String> tags;
}


