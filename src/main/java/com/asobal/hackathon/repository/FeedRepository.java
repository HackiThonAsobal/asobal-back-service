package com.asobal.hackathon.repository;

import com.asobal.hackathon.model.Feed;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FeedRepository extends MongoRepository<Feed, String> {

}
