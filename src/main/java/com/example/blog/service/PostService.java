package com.example.blog.service;

import com.example.blog.api.request.PostRequest;
import com.example.blog.api.response.PostByIdResponse;
import com.example.blog.api.response.PostListResponse;
import com.example.blog.api.response.PostResponse;
import com.example.blog.dto.PostDto;
import com.example.blog.model.ModerationStatus;
import com.example.blog.model.Post;
import com.example.blog.model.Tag;
import com.example.blog.model.User;
import com.example.blog.repository.PostRepository;
import com.example.blog.repository.TagRepository;
import com.example.blog.repository.UserRepository;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Post Service.
 */

@Service
public class PostService {

  private final PostRepository postRepository;
  private final MapperService mapperService;
  private final UserRepository userRepository;
  private final TagRepository tagRepository;

  public PostService(PostRepository postRepository,
      MapperService mapperService, UserRepository userRepository,
      TagRepository tagRepository) {
    this.postRepository = postRepository;
    this.mapperService = mapperService;
    this.userRepository = userRepository;
    this.tagRepository = tagRepository;
  }

  private PostListResponse getResponse(Page<Post> page) {
    PostListResponse postResponse = new PostListResponse();
    ArrayList<Post> posts = new ArrayList<>(page.getContent());
    List<PostDto> postDtoList = posts.stream()
        .map(mapperService::convertPostToDto)
        .collect(Collectors.toList());
    if (posts.isEmpty()) {
      return postResponse;
    }
    postResponse.setCount((int) page.getTotalElements());
    postResponse.setPosts(postDtoList);
    return postResponse;
  }

  private Pageable getPageable(int offset, int limit) {
    return PageRequest.of(offset / limit, limit);
  }

  public PostListResponse getAllPosts(int offset, int limit, String mode) {
    Page<Post> page;
    switch (mode) {
      case "popular":
        page = postRepository.findPopularPosts(getPageable(offset, limit));
        break;
      case "early":
        page = postRepository.findEarlyPosts(getPageable(offset, limit));
        break;
      case "best":
        page = postRepository.findBestPosts(getPageable(offset, limit));
        break;
      default:
        page = postRepository.findRecentPosts(getPageable(offset, limit));
    }
    return getResponse(page);
  }

  public PostListResponse searchPosts(int offset, int limit, String query) {
    Page<Post> page = postRepository.postsSearch(query, getPageable(offset, limit));
    return getResponse(page);
  }

  public PostListResponse getPostsByDate(int offset, int limit, String date) {
    Page<Post> page = postRepository.findPostsByDate(date, getPageable(offset, limit));
    return getResponse(page);
  }

  public PostListResponse getPostsByTag(int offset, int limit, String tag) {
    Page<Post> page = postRepository.findPostsByTags(tag, getPageable(offset, limit));
    return getResponse(page);
  }

  public boolean findPostById(int id) {
    return postRepository.findById(id).isPresent();
  }

  public PostByIdResponse getPostById(int id, Principal principal) {
    PostByIdResponse postResponse = new PostByIdResponse();
    Optional<Post> optionalPost = postRepository.findById(id);
    Optional<User> optionalUser;
    User user;
    if (principal != null) {
      optionalUser = userRepository.findByEmail(principal.getName());
      user = optionalUser.orElse(null);
      assert user != null;
      if (user.getIsModerator() != 1 && optionalPost.isPresent()
          && !user.equals(optionalPost.get().getUser())) {
        postViewIncrease(optionalPost.get());
      }
    }
    if (principal == null && optionalPost.isPresent()) {
      postViewIncrease(optionalPost.get());
    }
    optionalPost.ifPresent(value -> {
      PostDto postDto = mapperService.convertPostToDto(value);
      postResponse.setId(postDto.getId());
      postResponse.setTimestamp(postDto.getTime());
      postResponse.setActive(postDto.isActive());
      postResponse.setUserDto(postDto.getUserDto());
      postResponse.setTitle(postDto.getTitle());
      postResponse.setText(postDto.getText());
      postResponse.setLikeCount(postDto.getLikeCount());
      postResponse.setDislikeCount(postDto.getDislikeCount());
      postResponse.setViewCount(postDto.getViewCount());
      postResponse.setComments(postDto.getCommentsDto());
      postResponse.setTags(postDto.getTagsDto());
    });
    return postResponse;
  }

  private void postViewIncrease(Post post) {
    int viewsCount;
    viewsCount = post.getViewCount() + 1;
    post.setViewCount(viewsCount);
    postRepository.save(post);
  }

  public PostListResponse getMyPosts(String email, int offset, int limit, String status) {
    Page<Post> page;
    switch (status) {
      case "inactive":
        page = postRepository.findMyInactivePosts(email, getPageable(offset, limit));
        break;
      case "pending":
        page = postRepository.findMyPendingPosts(email, getPageable(offset, limit));
        break;
      case "declined":
        page = postRepository.findMyDeclinedPosts(email, getPageable(offset, limit));
        break;
      default:
        page = postRepository.findMyPublishedPosts(email, getPageable(offset, limit));
    }
    return getResponse(page);
  }

  public PostListResponse getPostsModeration(int offset, int limit, String status) {
    Page<Post> page;
    switch (status) {
      case "new":
        page = postRepository.findPostsByModerationStatus_New(getPageable(offset, limit));
        break;
      case "declined":
        page = postRepository.findPostsByModerationStatus_Declined(getPageable(offset, limit));
        break;
      default:
        page = postRepository.findPostsByModerationStatus_Accepted(getPageable(offset, limit));
    }
    return getResponse(page);
  }

  public PostResponse addPost(Principal principal, PostRequest postRequest) {
    PostResponse postResponse = new PostResponse();
    Map<String, String> errorsMap = new LinkedHashMap<>();
    Optional<User> optionalUser = userRepository.findByEmail(principal.getName());
    List<Tag> tagList = tagRepository.findAll();
    Set<Tag> tags = new HashSet<>(tagList);
    if (optionalUser.isPresent() && postRequest.getTitle().length() > 3
        && postRequest.getText().length() > 50) {
      User user = optionalUser.get();
      Post post = new Post();
      post.setUser(user);
      post.setTime(new Date());
      post.setModerationStatus(ModerationStatus.NEW);
      post.setIsActive(postRequest.getActive());
      post.setTitle(postRequest.getTitle());
      post.setTags(tags);
      post.setText(postRequest.getText());
      postRepository.save(post);
      postResponse.setResult(true);
    }
    if (postRequest.getTitle().length() < 3) {
      errorsMap.put("title", "Заголовок не установлен");
    }
    if (postRequest.getText().length() < 50) {
      errorsMap.put("text", "Текст публикации слишком короткий");
    }
    postResponse.setErrors(errorsMap);
    return postResponse;
  }
}
