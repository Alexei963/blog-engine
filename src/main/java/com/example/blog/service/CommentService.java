package com.example.blog.service;

import com.example.blog.api.request.CommentRequest;
import com.example.blog.api.response.CommentResponse;
import com.example.blog.api.response.ResultAndErrorsResponse;
import com.example.blog.dto.CommentDto;
import com.example.blog.model.Post;
import com.example.blog.model.PostComment;
import com.example.blog.model.User;
import com.example.blog.repository.CommentRepository;
import com.example.blog.repository.PostRepository;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Comment Service.
 */

@Service
public class CommentService {

  static final Logger logger = LoggerFactory.getLogger(CommentService.class);

  private final CommentRepository commentRepository;
  private final PostRepository postRepository;
  private final MapperService mapperService;
  private final UserService userService;

  public CommentService(CommentRepository commentRepository, PostRepository postRepository,
      MapperService mapperService, UserService userService) {
    this.commentRepository = commentRepository;
    this.postRepository = postRepository;
    this.mapperService = mapperService;
    this.userService = userService;
  }

  public CommentResponse addComment(CommentRequest commentRequest) {
    Optional<Post> postOptional = postRepository.findById(commentRequest.getPostId());
    Post post = null;
    if (postOptional.isPresent()) {
      post = postOptional.get();
    }
    PostComment comment = new PostComment();
    if (commentRequest.getParentId() == 0) {
      comment.setParentId(0);
    } else {
      comment.setParentId(commentRequest.getParentId());
    }
    User user = userService.getLoggedUser();
    comment.setPost(post);
    comment.setUser(user);
    comment.setTime(new Date());
    comment.setText(commentRequest.getText());
    commentRepository.save(comment);
    CommentResponse commentResponse = new CommentResponse();
    CommentDto commentDto = mapperService.convertCommentToDto(comment);
    commentResponse.setId(commentDto.getId());
    assert post != null;
    logger.info("Пользователь {} оставил комментарий по постом {}.", user.getName(), post.getId());
    return commentResponse;
  }

  public ResultAndErrorsResponse commentAddingErrors(CommentRequest commentRequest) {
    Map<String, String> errors = new HashMap<>();
    List<Integer> listIdComments = StreamSupport
        .stream(commentRepository.findAll().spliterator(), false)
        .map(PostComment::getId)
        .collect(Collectors.toList());
    List<Integer> listParentIdComments = StreamSupport
        .stream(commentRepository.findAll().spliterator(), false)
        .map(PostComment::getParentId)
        .collect(Collectors.toList());
    List<Integer> listIdPosts = StreamSupport
        .stream(postRepository.findAll().spliterator(), false)
        .map(Post::getId)
        .collect(Collectors.toList());
    if (commentRequest.getParentId() != 0
        && !listParentIdComments.contains(commentRequest.getParentId())
        && !listIdComments.contains(commentRequest.getParentId())) {
      errors.put("parent_id", "Такого комментария не существует");
    }
    if (!listIdPosts.contains(commentRequest.getPostId())) {
      errors.put("post_id", "Такого поста не существует");
    }
    if (commentRequest.getText() == null || commentRequest.getText().length() < 10) {
      errors.put("text", "Текст комментария не задан или слишком короткий");
    }
    ResultAndErrorsResponse postResponse = new ResultAndErrorsResponse();
    postResponse.setErrors(errors);
    return postResponse;
  }
}
