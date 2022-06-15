package com.example.blog.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

/**
 *  Comment Dto.
 */

@Getter
@Setter
@JsonPropertyOrder({"id", "timestamp", "text", "user"})
public class CommentDto {
  private int id;
  @JsonProperty("timestamp")
  private long time;
  private String text;
  @JsonProperty("user")
  @JsonIgnoreProperties({"email", "moderation", "moderationCount", "settings"})
  private UserDto userDto;
}
