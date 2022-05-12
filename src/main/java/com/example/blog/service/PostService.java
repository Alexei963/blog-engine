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
import org.springframework.data.domain.Sort;
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
    Iterable<Post> iterable = getAllPost(offset, limit, mode);
    Map<String, Object> responseMap;
    ArrayList<Map<String, Object>> posts = new ArrayList<>();
    for (Post post : iterable) {
      responseMap = new LinkedHashMap<>();
      responseMap.put("id", post.getId());
      responseMap.put("timestamp",  post.getTime().getTime());
      Map<String, Object> usersMap = new LinkedHashMap<>();
      usersMap.put("id", post.getUser().getId());
      usersMap.put("name", post.getUser().getName());
      responseMap.put("user", usersMap);
      responseMap.put("title", post.getTitle());
      responseMap.put("announce", post.getText());
      responseMap.put("likeCount", post.getVotes().size());
      responseMap.put("dislikeCount", post.getVotes().size());
      responseMap.put("commentCount", post.getComments().size());
      responseMap.put("viewCount", post.getViewCount());
      posts.add(responseMap);
    }
    if (posts.isEmpty()) {
      return postResponse;
    }
    postResponse.setCount(posts.size());
    postResponse.setPosts(posts);
    return postResponse;
  }

  public List<Post> getAllPost(int offset, int limit, String mode) {
    Pageable pageable = null;
    if (mode.equals("recent")) {
      pageable = PageRequest.of(offset, limit, Sort.by("time").descending());
    }
    if (mode.equals("popular")) {
      pageable = PageRequest.of(offset, limit, Sort.by("commentCount").descending());
    }
    if (mode.equals("best")) {
      pageable = PageRequest.of(offset, limit, Sort.by("likeCount").descending());
    }
    if (mode.equals("early")) {
      pageable = PageRequest.of(offset, limit, Sort.by("time").ascending());
    }
    Page<Post> page = postRepository.findAll(pageable);
    return page.stream().collect(Collectors.toList());
  }
}
