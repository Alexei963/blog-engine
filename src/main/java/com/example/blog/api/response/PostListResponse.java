package com.example.blog.api.response;

import com.example.blog.dto.PostDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *  Post list response.
 */

@Getter
@Setter
public class PostListResponse {

  private int count;
  @JsonIgnoreProperties(value = {"active", "text", "comments", "tags", "photo", "{user.id}"})
  private List<PostDto> posts = new ArrayList<>();
}
