package com.asobal.hackathon.repository;

import com.asobal.hackathon.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    List<User> findAllByEmail(String mail);

    long count();
}
