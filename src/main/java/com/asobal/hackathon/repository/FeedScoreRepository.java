package com.asobal.hackathon.repository;

import com.asobal.hackathon.model.FeedScore;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FeedScoreRepository extends MongoRepository<FeedScore, String> {
    List<FeedScore> findByUserIdAndType(String id, String type);
}
