package com.example.blog.service;

import com.example.blog.api.request.ModerationRequest;
import com.example.blog.api.response.ResultResponse;
import com.example.blog.model.ModerationStatus;
import com.example.blog.model.Post;
import com.example.blog.model.User;
import com.example.blog.repository.PostRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * Moderation Service.
 */

@Service
public class ModerationService {

  private final PostRepository postRepository;
  private final UserService userService;

  public ModerationService(PostRepository postRepository, UserService userService) {
    this.postRepository = postRepository;
    this.userService = userService;
  }

  public ResultResponse postModeration(ModerationRequest moderationRequest) {
    ResultResponse result = new ResultResponse();
    Optional<Post> postOptional = postRepository.findById((Integer) moderationRequest.getPostId());
    User user = userService.getLoggedUser();
    Post post = null;
    if (postOptional.isPresent()) {
      post = postOptional.get();
    }
    assert post != null;
    assert user != null;
    if (user.getIsModerator() == 1) {
      if (moderationRequest.getDecision().equals("accept")) {
        post.setModerationStatus(ModerationStatus.ACCEPTED);
        post.setModeratorId(user.getId());
      }
      if (moderationRequest.getDecision().equals("decline")) {
        post.setModerationStatus(ModerationStatus.DECLINED);
        post.setModeratorId(user.getId());
      }
      postRepository.save(post);
      result.setResult(true);
    }
    return result;
  }
}
