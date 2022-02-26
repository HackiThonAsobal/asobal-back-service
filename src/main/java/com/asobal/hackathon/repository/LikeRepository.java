package com.asobal.hackathon.repository;

import com.asobal.hackathon.model.Like;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LikeRepository extends MongoRepository<Like, String>{

    List<Like> findByUserId(String userId);

    List<Like> findByPostId(String postId);
}
