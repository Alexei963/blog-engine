package com.example.blog.service;

import com.example.blog.api.response.StatisticsResponse;
import com.example.blog.model.ModerationStatus;
import com.example.blog.model.Post;
import com.example.blog.model.PostVote;
import com.example.blog.model.User;
import com.example.blog.repository.PostRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * Statistics Service.
 */

@Service
public class StatisticsService {

  private final UserService userService;
  private final PostRepository postRepository;

  public StatisticsService(UserService userService,
      PostRepository postRepository) {
    this.userService = userService;
    this.postRepository = postRepository;
  }

  public StatisticsResponse getMyStatistics() {
    User user = userService.getLoggedUser();
    List<Post> posts = user.getPosts().stream()
        .filter(post -> post
            .getModerationStatus()
            .equals(ModerationStatus.ACCEPTED)
            && post.getIsActive() == 1)
        .collect(Collectors.toList());
    return getStatistics(posts);
  }

  public StatisticsResponse getAllStatistics() {
    List<Post> posts = postRepository.allAcceptedPosts();
    return getStatistics(posts);
  }

  private StatisticsResponse getStatistics(List<Post> posts) {
    StatisticsResponse response = new StatisticsResponse();
    response.setPostsCount(posts.size());
    List<PostVote> postVoteList = new ArrayList<>();
    posts.stream()
        .map(Post::getVotes)
        .forEach(postVoteList::addAll);
    long likesCount = postVoteList.stream()
        .mapToInt(PostVote::getValue)
        .filter(i -> i == 1)
        .count();
    response.setLikesCount((int) likesCount);
    long dislikesCount = postVoteList.stream()
        .mapToInt(PostVote::getValue)
        .filter(i -> i == -1)
        .count();
    response.setDislikesCount((int) dislikesCount);
    int viewsCount = posts.stream()
        .mapToInt(Post::getViewCount)
        .sum();
    response.setViewsCount(viewsCount);
    Optional<Long> firstPublication = posts.stream()
        .map(Post::getTime)
        .map(Date::getTime)
        .min(Long::compare);
    firstPublication.ifPresent(a -> response.setFirstPublication(a / 1000));
    return response;
  }
}
