package com.example.blog.api.response;

import com.example.blog.dto.PostDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 *  Post list response.
 */

@Data
public class PostListResponse {

  private int count;
  @JsonIgnoreProperties(value = {"active", "text", "comments", "tags", "photo", "{user.id}"})
  private List<PostDto> posts = new ArrayList<>();
}
