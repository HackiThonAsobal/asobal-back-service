package com.asobal.hackathon.domain.request;

import lombok.Data;

@Data
public class UserRequest {
    private String name;
    private String lastName;
    private String gender;
    private String teamId;
    private String birthDate;
}
