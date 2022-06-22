package com.example.blog.controller;

import com.example.blog.api.response.PostByIdResponse;
import com.example.blog.api.response.PostListResponse;
import com.example.blog.service.PostService;
import java.security.Principal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
  private ResponseEntity<PostListResponse> getAllPosts(
      @RequestParam(defaultValue = "0") int offset,
      @RequestParam(defaultValue = "10") int limit,
      @RequestParam(defaultValue = "recent") String mode) {
    return new ResponseEntity<>(postService.getAllPosts(offset, limit, mode), HttpStatus.OK);
  }

  @GetMapping("/search")
  private ResponseEntity<PostListResponse> searchPosts(
      @RequestParam(defaultValue = "0") int offset,
      @RequestParam(defaultValue = "10") int limit,
      @RequestParam(required = false) String query) {
    return new ResponseEntity<>(postService.searchPosts(offset, limit, query), HttpStatus.OK);
  }

  @GetMapping("/byDate")
  private ResponseEntity<PostListResponse> getPostsByDate(
      @RequestParam(defaultValue = "0") int offset,
      @RequestParam(defaultValue = "10") int limit,
      @RequestParam(required = false) String date) {
    return new ResponseEntity<>(postService.getPostsByDate(offset, limit, date), HttpStatus.OK);
  }

  @GetMapping("/byTag")
  private ResponseEntity<PostListResponse> getPostsByTag(
      @RequestParam(defaultValue = "0") int offset,
      @RequestParam(defaultValue = "10") int limit,
      @RequestParam(required = false) String tag) {
    return new ResponseEntity<>(postService.getPostsByTag(offset, limit, tag), HttpStatus.OK);
  }

  @GetMapping("/{id}")
  private ResponseEntity<PostByIdResponse> getPost(@PathVariable int id) {
    if (!(postService.getPost(id) == null)) {
      return new ResponseEntity<>(postService.getPost(id), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping("/my")
  private ResponseEntity<PostListResponse> getMyPosts(
      Principal principal,
      @RequestParam(defaultValue = "0") int offset,
      @RequestParam(defaultValue = "10") int limit,
      @RequestParam(required = false) String status
  ) {
    return new ResponseEntity<>(postService.getMyPosts(
        principal.getName(), offset, limit, status), HttpStatus.OK);
  }
}
