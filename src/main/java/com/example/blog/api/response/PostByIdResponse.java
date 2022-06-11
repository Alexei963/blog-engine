package com.example.blog.api.response;

import com.example.blog.dto.CommentDto;
import com.example.blog.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *  Post by id response.
 */

@Getter
@Setter
@JsonPropertyOrder({"id", "timestamp", "active", "user", "title", "text",
    "likeCount", "dislikeCount", "viewCount", "comments", "tags"})
public class PostByIdResponse {
  private int id;
  private long timestamp;
  private boolean active;
  @JsonProperty("user")
  @JsonIgnoreProperties({"photo", "email", "moderation", "moderationCount", "settings"})
  private UserDto userDto;
  private String title;
  private String text;
  private int likeCount;
  private int dislikeCount;
  private int viewCount;
  private List<CommentDto> comments;
  private List<String> tags;
}
