package com.example.blog.service;

import com.example.blog.api.response.PostResponse;
import com.example.blog.model.Post;
import com.example.blog.repository.PostRepository;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

  public PostService(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  public PostResponse getPostResponse(int offset, int limit, String mode) {
    PostResponse postResponse = new PostResponse();
    Pageable pageable = PageRequest.of(offset, limit);
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
    ArrayList<Map<String, Object>> posts
        = (ArrayList<Map<String, Object>>) mappingInResponse(page);
    if (posts.isEmpty()) {
      return postResponse;
    }
    postResponse.setCount(posts.size());
    postResponse.setPosts(posts);
    return postResponse;
  }

  private List<Map<String, Object>> mappingInResponse(Page<Post> page) {
    ArrayList<Map<String, Object>> posts = new ArrayList<>();
    Map<String, Object> responseMap;
    for (Post post : page) {
      String announce = post.getText();
      if (announce.length() > 150) {
        announce = announce.substring(0, 150).concat("...");
      }
      responseMap = new LinkedHashMap<>();
      responseMap.put("id", post.getId());
      responseMap.put("timestamp", post.getTime().getTime() / 1000);
      Map<String, Object> usersMap = new LinkedHashMap<>();
      usersMap.put("id", post.getUser().getId());
      usersMap.put("name", post.getUser().getName());
      responseMap.put("user", usersMap);
      responseMap.put("title", post.getTitle());
      responseMap.put("announce", announce);
      responseMap.put("likeCount", post.getVotes().size());
      responseMap.put("dislikeCount", post.getVotes().size());
      responseMap.put("commentCount", post.getComments().size());
      responseMap.put("viewCount", post.getViewCount());
      posts.add(responseMap);
    }
    return posts;
  }
}
