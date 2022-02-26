package com.asobal.hackathon.domain.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String lastName;
    @NotBlank
    private String gender;
    @NotBlank
    private String teamId;
    @NotBlank
    private String birthDate;
}
