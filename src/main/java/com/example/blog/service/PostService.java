package com.example.blog.service;

import com.example.blog.api.response.PostResponse;
import com.example.blog.dto.PostDto;
import com.example.blog.model.Post;
import com.example.blog.repository.PostRepository;
import java.util.ArrayList;
import java.util.List;
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

  public PostResponse getPostResponse(int offset, int limit, String mode) {
    PostResponse postResponse = new PostResponse();
    Pageable pageable = PageRequest.of(offset / limit, limit);
    Page<Post> page;
    switch (mode) {
      case "popular":
        page = postRepository.findPopularPosts(pageable);
        break;
      case "early":
        page = postRepository.findEarlyPosts(pageable);
        break;
      case "best":
        page = postRepository.findBestPosts(pageable);
        break;
      default:
        page = postRepository.findRecentPosts(pageable);
    }
    ArrayList<Post> posts = new ArrayList<>(page.getContent());
    List<PostDto> postDtoList = posts.stream()
        .map(mapperService::convertPostToDto)
        .collect(Collectors.toList());
    if (posts.isEmpty()) {
      return postResponse;
    }
    postResponse.setCount((int) postRepository.count());
    postResponse.setPosts(postDtoList);
    return postResponse;
  }
}
