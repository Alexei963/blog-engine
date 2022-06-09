package com.example.blog.api.response;

import com.example.blog.dto.PostDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PostResponse {
  @JsonIgnoreProperties(value = {"announce", "commentCount"})
  private PostDto postDto;
}
