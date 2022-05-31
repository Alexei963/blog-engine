package com.example.blog.controller;

import com.example.blog.api.response.PostResponse;
import com.example.blog.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Api Post Controller.
 */

@RestController
@RequestMapping("/api/post")
public class ApiPostController {

  private final PostService postService;

  public ApiPostController(PostService postService) {
    this.postService = postService;
  }

  @GetMapping("")
  private ResponseEntity<PostResponse> getPosts(
      @RequestParam(defaultValue = "0") int offset,
      @RequestParam(defaultValue = "10") int limit,
      @RequestParam(defaultValue = "recent") String mode) {
    return new ResponseEntity<>(postService.getPostResponse(offset, limit, mode), HttpStatus.OK);
  }

  @GetMapping("/search")
  private ResponseEntity<PostResponse> searchPosts(
      @RequestParam(defaultValue = "0") int offset,
      @RequestParam(defaultValue = "10") int limit,
      @RequestParam(required = false) String query) {
    return new ResponseEntity<>(postService.searchPosts(offset, limit, query), HttpStatus.OK);
  }

  @GetMapping("/byDate")
  private ResponseEntity<PostResponse> getPostsByDate(
      @RequestParam(defaultValue = "0") int offset,
      @RequestParam(defaultValue = "10") int limit,
      @RequestParam(required = false) String date) {
    return new ResponseEntity<>(postService.getPostsByDate(offset, limit, date), HttpStatus.OK);
  }

  @GetMapping("/byTag")
  private ResponseEntity<PostResponse> getPostsByTag(
      @RequestParam(defaultValue = "0") int offset,
      @RequestParam(defaultValue = "10") int limit,
      @RequestParam(required = false) String tag) {
    return new ResponseEntity<>(postService.getPostsByTag(offset, limit, tag), HttpStatus.OK);
  }
}
