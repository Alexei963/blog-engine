package com.example.blog.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Moderation Request.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModerationRequest {

  @JsonProperty("post_id")
  private int postId;
  private String decision;
}
