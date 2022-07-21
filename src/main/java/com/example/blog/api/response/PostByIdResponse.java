package com.example.blog.api.response;

import com.example.blog.dto.CommentDto;
import com.example.blog.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

/**
 *  Post by id response.
 */

@Data
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
