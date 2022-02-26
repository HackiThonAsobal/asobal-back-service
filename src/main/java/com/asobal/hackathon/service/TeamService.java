package com.asobal.hackathon.service;

import com.asobal.hackathon.domain.response.TeamResponse;

import java.util.List;

public interface TeamService {
    List<TeamResponse> findAllTeams();
}
