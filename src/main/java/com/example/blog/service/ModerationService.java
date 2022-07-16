package com.example.blog.service;

import com.example.blog.api.request.ModerationRequest;
import com.example.blog.api.response.ResultResponse;
import com.example.blog.model.ModerationStatus;
import com.example.blog.model.Post;
import com.example.blog.model.User;
import com.example.blog.repository.PostRepository;
import com.example.blog.repository.UserRepository;
import java.security.Principal;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * Moderation Service.
 */

@Service
public class ModerationService {

  private final PostRepository postRepository;
  private final UserRepository userRepository;

  public ModerationService(PostRepository postRepository, UserRepository userRepository) {
    this.postRepository = postRepository;
    this.userRepository = userRepository;
  }

  public ResultResponse postModeration(ModerationRequest moderationRequest, Principal principal) {
    ResultResponse result = new ResultResponse();
    Optional<User> userOptional = userRepository.findByEmail(principal.getName());
    Optional<Post> postOptional = postRepository.findById((Integer) moderationRequest.getPostId());
    User user = null;
    Post post = null;
    if (userOptional.isPresent()) {
      user = userOptional.get();
    }
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
