package com.example.blog.service;

import com.example.blog.api.request.PostIdRequest;
import com.example.blog.api.request.PostRequest;
import com.example.blog.api.response.PostByIdResponse;
import com.example.blog.api.response.PostListResponse;
import com.example.blog.api.response.ResultAndErrorsResponse;
import com.example.blog.api.response.ResultResponse;
import com.example.blog.dto.PostDto;
import com.example.blog.model.ModerationStatus;
import com.example.blog.model.Post;
import com.example.blog.model.PostVote;
import com.example.blog.model.Tag;
import com.example.blog.model.User;
import com.example.blog.repository.PostRepository;
import com.example.blog.repository.PostVoteRepository;
import com.example.blog.repository.TagRepository;
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
  private final TagRepository tagRepository;
  private final UserService userService;
  private final PostVoteRepository postVoteRepository;

  public PostService(PostRepository postRepository, MapperService mapperService,
      TagRepository tagRepository, UserService userService,
      PostVoteRepository postVoteRepository) {
    this.postRepository = postRepository;
    this.mapperService = mapperService;
    this.tagRepository = tagRepository;
    this.userService = userService;
    this.postVoteRepository = postVoteRepository;
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

  public PostByIdResponse getPostById(int id) {
    PostByIdResponse postResponse = new PostByIdResponse();
    Optional<Post> optionalPost = postRepository.findById(id);
    User user = userService.getLoggedUser();
    if (user != null) {
      if (user.getIsModerator() != 1 && optionalPost.isPresent()
          && !user.equals(optionalPost.get().getUser())) {
        postViewIncrease(optionalPost.get());
      }
    }
    if (user == null && optionalPost.isPresent()) {
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

  public ResultAndErrorsResponse addPost(PostRequest postRequest) {
    ResultAndErrorsResponse postResponse = new ResultAndErrorsResponse();
    User user = userService.getLoggedUser();
    if (user != null) {
      Post post = new Post();
      addOrEditPost(postResponse, postRequest, user, post, ModerationStatus.NEW);
    }
    return postResponse;
  }

  public ResultAndErrorsResponse editPost(int id, PostRequest postRequest) {
    ResultAndErrorsResponse postResponse = new ResultAndErrorsResponse();
    Optional<Post> optionalPost = postRepository.findById(id);
    User user = userService.getLoggedUser();
    if (optionalPost.isPresent() && user != null) {
      Post post = optionalPost.get();
      if (user.getIsModerator()  == 1) {
        addOrEditPost(postResponse, postRequest, post.getUser(), post, post.getModerationStatus());
      } else {
        addOrEditPost(postResponse, postRequest, post.getUser(), post, ModerationStatus.NEW);
      }
    }
    return postResponse;
  }

  private void addOrEditPost(ResultAndErrorsResponse postResponse, PostRequest postRequest,
      User user, Post post, ModerationStatus moderationStatus) {
    Map<String, String> errorsMap = new LinkedHashMap<>();
    Set<Tag> tags = new HashSet<>();
    postRequest.getTags().forEach(t -> {
      if (!tagRepository.findAll().stream()
          .map(Tag::getName)
          .collect(Collectors.toList())
          .contains(t)) {
        Tag tag = new Tag();
        tag.setName(t);
        tagRepository.save(tag);
      }
      Tag tag = tagRepository.findByName(t);
      tags.add(tag);
    });
    if (postRequest.getTitle().length() > 3
        && postRequest.getText().length() > 50) {
      post.setUser(user);
      Date date = new Date(postRequest.getTimestamp() * 1000);
      if (date.before(new Date())) {
        post.setTime(new Date());
      } else {
        post.setTime(date);
      }
      post.setModerationStatus(moderationStatus);
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
  }

  public ResultResponse getPostVote(PostIdRequest postIdRequest, int postVoteValue) {
    ResultResponse response = new ResultResponse();
    Optional<Post> postOptional = postRepository.findById(postIdRequest.getPostId());
    User user = userService.getLoggedUser();
    Post post;
    if (user != null && postOptional.isPresent()) {
      post = postOptional.get();
      List<User> postVotes = post.getVotes().stream()
          .map(PostVote::getUser)
          .collect(Collectors.toList());
      Optional<PostVote> optionalPostVote =
          postVoteRepository.findPostVoteByUserId(user.getId(), post.getId());
      if (optionalPostVote.isPresent() && postVotes.contains(user)) {
        PostVote postVote = optionalPostVote.get();
        if (postVote.getValue() == postVoteValue) {
          response.setResult(false);
        } else {
          postVote.setTime(new Date());
          postVote.setValue(postVoteValue);
          postVoteRepository.save(postVote);
          response.setResult(true);
        }
      } else {
        PostVote postVote = new PostVote();
        postVote.setUser(user);
        postVote.setPost(post);
        postVote.setTime(new Date());
        postVote.setValue(postVoteValue);
        postVoteRepository.save(postVote);
        response.setResult(true);
      }
    }
    return response;
  }
}
