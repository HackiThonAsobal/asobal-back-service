package com.asobal.hackathon.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document("user")
@Data
@Builder
public class User {
    @Id
    private String id;

    private String email;
    private String name = "";
    private String lastName = "";
    private String gender = "";
    private String teamId = "";
    private LocalDate birthDate = LocalDate.now();
    private String type = "user";
}
