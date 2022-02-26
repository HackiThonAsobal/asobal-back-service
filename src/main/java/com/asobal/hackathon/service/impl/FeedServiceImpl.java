package com.asobal.hackathon.service.impl;

import com.asobal.hackathon.domain.request.FeedContentRequest;
import com.asobal.hackathon.domain.response.CommentResponse;
import com.asobal.hackathon.domain.response.FeedResponse;
import com.asobal.hackathon.model.*;
import com.asobal.hackathon.repository.*;
import com.asobal.hackathon.service.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FeedServiceImpl implements FeedService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    FeedRepository feedRepository;

    @Autowired
    LikeRepository likeRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    FeedScoreRepository feedScoreRepository;

    @Override
    public List<FeedResponse> getFeedList(String email) {
        //Recuperamos la información del usuario
        User userInfo = userRepository.findAllByEmail(email).get(0);

        // Recuperamos la lista completa de noticas.
        List<Feed> listFeed = feedRepository.findAll();

        List<FeedResponse> result = listFeed.stream().map(x -> FeedResponse.builder()
                .id(x.getId())
                .title(x.getTitle())
                .description(x.getDescription())
                .image(x.getImage())
                .type(x.getType())
                .teamOneImage(x.getTeamOneImage())
                .teamTwoImage(x.getTeamTwoImage())
                .comments(x.getComments())
                .publicationDaTe(x.getPublicationDate())
                .matchDate(x.getMatchDate())
                .userId(x.getUserId())
                .tags(x.getTags())
                .build()
        ).toList();

        // Recuperamos las noticias a las que el usuario ha dado like.
        List<Like> likedPosts = likeRepository.findByUserId(userInfo.getId());

        // Agregamos los likes dados por el usuario a la lista
        List<FeedResponse> resultWithUserLikes = result.stream().map(x -> {
            FeedResponse a = new FeedResponse(
                    x.getId(),
                    x.getTitle(),
                    x.getDescription(),
                    x.getImage(),
                    x.getType(),
                    x.getTeamOneImage(),
                    x.getTeamTwoImage(),
                    x.getLikes(),
                    false,
                    x.getComments(),
                    x.getPublicationDaTe(),
                    x.getMatchDate(),
                    x.getUserId(),
                    x.getTags()
            );
            if (likedPosts.stream().anyMatch(y -> y.getPostId().equals(x.getId()))) {
                a.setIsLiked(true);
            }
            return a;
        }).toList();

        // Agregamos el número total de likes que tiene cada feed
        List<FeedResponse> resultWithFullLikeInfo = resultWithUserLikes.stream().map(x -> {
            FeedResponse a = new FeedResponse(
                    x.getId(),
                    x.getTitle(),
                    x.getDescription(),
                    x.getImage(),
                    x.getType(),
                    x.getTeamOneImage(),
                    x.getTeamTwoImage(),
                    x.getLikes(),
                    x.getIsLiked(),
                    x.getComments(),
                    x.getPublicationDaTe(),
                    x.getMatchDate(),
                    x.getUserId(),
                    x.getTags()
            );
            a.setLikes(likeRepository.findByPostId(x.getId()).size());
            return a;
        }).toList();

        return getResultRatedByUser(userInfo, resultWithFullLikeInfo);
    }

    @Override
    public void setUpFeedContent(String email, FeedContentRequest feedContentRequest) {
        LocalDateTime now = LocalDateTime.now();
        User userInfo = userRepository.findAllByEmail(email).get(0);
        if (feedContentRequest.getType().equals("COMMENT")) {
            Optional<Feed> fatherFeedItem = feedRepository.findById(feedContentRequest.getLinkedPostId());
            fatherFeedItem.get().getComments().add(
                    CommentResponse.builder()
                            .description(feedContentRequest.getDescription())
                            .userName(userInfo.getName() + " " + userInfo.getLastName())
                            .userId(userInfo.getId())
                            .commentDate(now)
                            .build()
            );
            fatherFeedItem.get().setLastModificationDate(now);
            feedRepository.save(fatherFeedItem.get());
        } else {
            Set<String> tags = findPostTags(userInfo, feedContentRequest.getDescription());

            feedRepository.save(
                    Feed.builder()
                            .title(feedContentRequest.getTitle())
                            .description(feedContentRequest.getDescription())
                            .image(feedContentRequest.getImage())
                            .type(feedContentRequest.getType())
                            .publicationDate(now)
                            .lastModificationDate(now)
                            .userId(userInfo.getId())
                            .comments(new ArrayList<>())
                            .tags(tags.stream().toList())
                            .build()
            );
        }
    }


    @Override
    public void likeAction(String email, String postId) {
        User userInfo = userRepository.findAllByEmail(email).get(0);
        Optional<Like> optLike = likeRepository.findByPostIdAndUserId(postId, userInfo.getId());
        if (optLike.isPresent()) {
            likeRepository.delete(optLike.get());
        } else {
            likeRepository.save(Like.builder().postId(postId).userId(userInfo.getId()).build());
        }
    }

    private Set<String> findPostTags(User userInfo, String description) {
        Set<String> result = new HashSet<>();
        // Buscamos el tag del equipo del ususario
        Optional<Team> userTeam = teamRepository.findById(userInfo.getTeamId());
        userTeam.ifPresent(team -> result.add(team.getTag()));

        // Buscamos tags en el comentario del usuario
        while (description.indexOf('#') != -1) {
            int start = description.indexOf('#');
            int finish = description.indexOf(' ', start) != -1 ? description.indexOf(' ', start) : description.length();
            result.add(description.substring(start + 1, finish));
            description = description.substring(finish);
        }

        return result;
    }

    private List<FeedResponse> getResultRatedByUser(User userInfo, List<FeedResponse> feed) {
        // El tipo de publicación para el Feed siempre será "FEED"
        final String _TYPE = "FEED";
        // Recuperamos los feeds ya visitados por el usuario
        List<FeedScore> feedScores = feedScoreRepository.findByUserIdAndType(userInfo.getId(), _TYPE);
        // Consideramos que ha visto una publicación cuando la ha tenido en pantalla más de 2 segundos
        List<FeedScore> feedScoresElegibles = feedScores.stream().filter(x -> x.getTimeSpent() > 2000).toList();

        // Publicaciones del usuario con comentarios nuevos
        Comparator<FeedResponse> myPostsWithNewComments = (x, y) -> {
            Optional<FeedScore> lastSeenX = feedScores.stream()
                    .filter(feedX -> feedX.getObjectId().equals(x.getId())).min(Comparator.comparing(FeedScore::getDate));
            Optional<FeedScore> lastSeenY = feedScores.stream()
                    .filter(feedY -> feedY.getObjectId().equals(y.getId())).min(Comparator.comparing(FeedScore::getDate));
            // Comprobamos si x e y ambos o ninguno son del usuario y tienen comentarios pendientes de visualizar
            boolean isXMine = x.getUserId().equals(userInfo.getId());
            boolean isYMine = y.getUserId().equals(userInfo.getId());
            boolean hasXPendingComments = lastSeenX.isPresent()
                    && x.getComments().stream()
                    .anyMatch(com -> com.getCommentDate().isAfter(lastSeenX.get().getDate()));
            boolean hasYPendingComments = lastSeenY.isPresent()
                    && y.getComments().stream()
                    .anyMatch(com -> com.getCommentDate().isAfter(lastSeenY.get().getDate()));
            if (isXMine && !isYMine) {
                if (hasXPendingComments) return -1;
            } else if (isYMine && !isXMine) {
                if (hasYPendingComments) return 1;
            } else if (isXMine && isYMine) {
                if (hasXPendingComments && !hasYPendingComments) return -1;
                else if (hasYPendingComments && !hasXPendingComments) return 1;
            }
            return x.getPublicationDaTe().compareTo(y.getPublicationDaTe());
        };

        // Publicaciones en las que ha comentado y hay nuevas respuestas de otros usuarios
        Comparator<FeedResponse> commentedPostsWithNewComments = (x, y) -> {
            Optional<FeedScore> lastSeenX = feedScores.stream()
                    .filter(feedX -> feedX.getObjectId().equals(x.getId())).min(Comparator.comparing(FeedScore::getDate));
            Optional<FeedScore> lastSeenY = feedScores.stream()
                    .filter(feedY -> feedY.getObjectId().equals(y.getId())).min(Comparator.comparing(FeedScore::getDate));

            boolean haveICommentedX = x.getComments().stream()
                    .anyMatch(com -> com.getUserId().equals(userInfo.getId()));
            boolean haveICommentedY = y.getComments().stream()
                    .anyMatch((com -> com.getUserId().equals(userInfo.getId())));
            boolean hasXPendingComments = lastSeenX.isPresent()
                    && x.getComments().stream()
                    .anyMatch(com -> com.getCommentDate().isAfter(lastSeenX.get().getDate()));
            boolean hasYPendingComments = lastSeenY.isPresent()
                    && y.getComments().stream()
                    .anyMatch(com -> com.getCommentDate().isAfter(lastSeenY.get().getDate()));
            if (haveICommentedX && !haveICommentedY) {
                if (hasXPendingComments) return -1;
            } else if (haveICommentedY && !haveICommentedX) {
                if (hasYPendingComments) return 1;
            } else if (haveICommentedX && haveICommentedY) {
                if (hasXPendingComments && !hasYPendingComments) return -1;
                else if (hasYPendingComments && !hasXPendingComments) return 1;
            }
            return x.getPublicationDaTe().compareTo(y.getPublicationDaTe());
        };

        Optional<Team> team = teamRepository.findById(userInfo.getTeamId());
        // Obtenemos las publicaciones no vistas del equipo del usuario
        Comparator<FeedResponse> notWatchedBeforeOfMyTeam = (x, y) -> {
            boolean haveIWatchedX = feedScores.stream().anyMatch(feedX -> feedX.getObjectId().equals(x.getId()));
            boolean haveIWatchedY = feedScores.stream().anyMatch(feedY -> feedY.getObjectId().equals(y.getId()));
            boolean isXofMyTeam = team.isPresent() && x.getTags()
                    .stream().anyMatch(tag -> tag.equals(team.get().getTag()));
            boolean isYofMyTeam = team.isPresent() && y.getTags()
                    .stream().anyMatch(tag -> tag.equals(team.get().getTag()));
            if (haveIWatchedX && !haveIWatchedY) {
                if (isYofMyTeam) {
                    return 1;
                }
            } else if (haveIWatchedY && !haveIWatchedX) {
                if (isXofMyTeam) {
                    return -1;
                }
            } else if (!haveIWatchedX && !haveIWatchedY) {
                if (isXofMyTeam && !isYofMyTeam) {
                    return -1;
                }
                if (isYofMyTeam && !isXofMyTeam) {
                    return 1;
                }
            }
            return x.getPublicationDaTe().compareTo(y.getPublicationDaTe());
        };

        // Calculamos los tags con más valor para el usuario.
        //List<String> tagList = calculateMostValuableTags(feedScores, feed);
        //TODO desarrollar este último comparador
        Comparator<FeedResponse> notWatchedWithMyPreferences = (x, y ) -> {return -1;};

        // Ordenamos por los patrones que nos interesen
        // - Publicaciones del usuario con comentarios nuevos
        // - Publicaciones en las que ha comentado y hay nuevas respuestas de otros usuarios
        // - Publicaciones no vistas
        //   * Del equipo del usuario
        //   * Que contengan alguno de los 3 tags mas interesantes para el usuario
        // Publicaciones previamente visualizadas por orden de creación.
        return feed.stream()
                .sorted(myPostsWithNewComments
                        .thenComparing(commentedPostsWithNewComments)
                        .thenComparing(notWatchedBeforeOfMyTeam)
                        .thenComparing(notWatchedWithMyPreferences)
                )
                .collect(Collectors.toList());
    }

    private List<String> calculateMostValuableTags(List<FeedScore> feedScores, List<FeedResponse> feed) {
        List<String> result = new ArrayList<>();
        HashMap<String, Long> tagValue = new HashMap();
        // Sumamos a cada tag el tiempo que se ha visualizado entre toda la feed.
        feed.forEach(item -> {
            long totalTime = feedScores.stream()
                    .filter(scores -> scores.getObjectId().equals(item.getId()))
                    .map(FeedScore::getTimeSpent).mapToLong(Long::longValue).sum();
            item.getTags().forEach(tag -> {
                tagValue.put(tag, tagValue.get(tag) + totalTime);
            });
        });

        //Devolvemos los tres tags con más tiempo de visualización
        return tagValue.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

}
