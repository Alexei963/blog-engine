package com.example.blog.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Post Id Request.
 */

@Data
public class PostIdRequest {

  @JsonProperty("post_id")
  private int postId;
}
