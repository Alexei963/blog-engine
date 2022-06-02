package com.example.blog.api.response;

import com.example.blog.dto.PostDto;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Post Response.
 */

@Getter
@Setter
public class PostListResponse {

  private int count;
  private List<PostDto> posts = new ArrayList<>();
}
