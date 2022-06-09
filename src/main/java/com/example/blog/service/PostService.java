package com.example.blog.service;

import com.example.blog.api.response.PostListResponse;
import com.example.blog.api.response.PostResponse;
import com.example.blog.dto.PostDto;
import com.example.blog.model.Post;
import com.example.blog.repository.PostRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

  public PostService(PostRepository postRepository,
      MapperService mapperService) {
    this.postRepository = postRepository;
    this.mapperService = mapperService;
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

  public PostResponse getPost(int id) {
    PostResponse postResponse = new PostResponse();
    Optional<Post> post = postRepository.findById(id);
    post.ifPresent(value -> postResponse.setPostDto(mapperService.convertOnePostToDto(value)));
    return postResponse;
  }
}
