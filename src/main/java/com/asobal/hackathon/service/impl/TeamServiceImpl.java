package com.asobal.hackathon.service.impl;

import com.asobal.hackathon.domain.response.TeamResponse;
import com.asobal.hackathon.model.Team;
import com.asobal.hackathon.repository.TeamRepository;
import com.asobal.hackathon.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamServiceImpl implements TeamService {
    @Autowired
    TeamRepository teamRepository;

    @Override
    public List<TeamResponse> findAllTeams() {
        List<Team> response = teamRepository.findAll();
        return response.stream().map(x ->
                        TeamResponse.builder()
                                .id(x.getId())
                                .name(x.getName())
                                .logo(x.getLogo())
                                .score(x.getScore())
                                .position(x.getPosition())
                                .build()
                )
                .collect(Collectors.toList());
    }
}
