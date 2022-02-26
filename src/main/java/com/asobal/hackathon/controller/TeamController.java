package com.asobal.hackathon.controller;

import com.asobal.hackathon.domain.response.TeamResponse;
import com.asobal.hackathon.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TeamController {

    @Autowired
    TeamService teamService;

    @GetMapping("/api/teams")
    public List<TeamResponse> getTeams(){
        return teamService.findAllTeams();
    }
}
