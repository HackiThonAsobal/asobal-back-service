package com.asobal.hackathon.service.impl;

import com.asobal.hackathon.domain.request.FeedContentRequest;
import com.asobal.hackathon.domain.response.CommentResponse;
import com.asobal.hackathon.domain.response.FeedResponse;
import com.asobal.hackathon.model.Feed;
import com.asobal.hackathon.model.Like;
import com.asobal.hackathon.model.User;
import com.asobal.hackathon.repository.FeedRepository;
import com.asobal.hackathon.repository.LikeRepository;
import com.asobal.hackathon.repository.UserRepository;
import com.asobal.hackathon.service.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FeedServiceImpl implements FeedService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    FeedRepository feedRepository;

    @Autowired
    LikeRepository likeRepository;

    @Override
    public List<FeedResponse> getFeedList(String email) {
        //Recuperamos la información del usuario
        User userInfo = userRepository.findAllByEmail(email).get(0);

        // Recuperamos la lista completa de noticas.
        List<Feed> listFeed = feedRepository.findAll();

        List<FeedResponse> result = listFeed.stream().map(x -> FeedResponse.builder()
                .id(x.getId())
                .tittle(x.getTittle())
                .description(x.getDescription())
                .image(x.getImage())
                .type(x.getType())
                .teamOneImage(x.getTeamOneImage())
                .teamTwoImage(x.getTeamTwoImage())
                .comments(x.getComments())
                .publicationDaTe(x.getPublicationDaTe())
                .matchDate(x.getMatchDate())
                .build()
        ).toList();

        // Recuperamos las noticias a las que el usuario ha dado like.
        List<Like> likedPosts = likeRepository.findByUserId(userInfo.getId());

        // Agregamos los likes dados por el usuario a la lista
        List<FeedResponse> resultWithUserLikes = result.stream().map(x -> {
            FeedResponse a = new FeedResponse(
                    x.getId(),
                    x.getTittle(),
                    x.getDescription(),
                    x.getImage(),
                    x.getType(),
                    x.getTeamOneImage(),
                    x.getTeamTwoImage(),
                    x.getLikes(),
                    false,
                    x.getComments(),
                    x.getPublicationDaTe(),
                    x.getMatchDate()
            );
            if (likedPosts.stream().filter(y -> y.getId().equals(x.getId())).findAny().isPresent()) {
                x.setIsLiked(true);
            }
            return a;
        }).toList();

        // Agregamos el número total de likes que tiene cada feed
        List<FeedResponse> resultWithFullLikeInfo = resultWithUserLikes.stream().map(x -> {
            FeedResponse a = new FeedResponse(
                    x.getId(),
                    x.getTittle(),
                    x.getDescription(),
                    x.getImage(),
                    x.getType(),
                    x.getTeamOneImage(),
                    x.getTeamTwoImage(),
                    x.getLikes(),
                    x.getIsLiked(),
                    x.getComments(),
                    x.getPublicationDaTe(),
                    x.getMatchDate()
            );
            a.setLikes(likeRepository.findByPostId(x.getId()).size());
            return a;
        }).toList();

        return resultWithFullLikeInfo;
    }

    @Override
    public void setUpFeedContent(String email, FeedContentRequest feedContentRequest) {
        User userInfo = userRepository.findAllByEmail(email).get(0);
        if (feedContentRequest.getType().equals("COMMENT")) {
            Optional<Feed> fatherFeedItem = feedRepository.findById(feedContentRequest.getLinkedPostId());
            fatherFeedItem.get().getComments().add(
                    CommentResponse.builder()
                            .description(feedContentRequest.getDescription())
                            .userName(userInfo.getName() + " " +userInfo.getLastName())
                            .userId(userInfo.getId())
                            .build()
            );
            feedRepository.save(fatherFeedItem.get());
        } else {
            feedRepository.save(
                    Feed.builder()
                            .tittle(feedContentRequest.getTittle())
                            .description(feedContentRequest.getDescription())
                            .image(feedContentRequest.getImage())
                            .type(feedContentRequest.getType())
                            .publicationDaTe(LocalDateTime.now())
                            .userId(userInfo.getId())
                            .comments(new ArrayList<>())
                            .build()
            );
        }
    }
}
