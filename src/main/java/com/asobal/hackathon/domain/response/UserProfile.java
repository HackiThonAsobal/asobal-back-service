package com.asobal.hackathon.domain.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserProfile {
    private String name;
    private String lastName;
    private String gender;
    private String teamId;
    private LocalDate birthDate;
    private String type;
}
