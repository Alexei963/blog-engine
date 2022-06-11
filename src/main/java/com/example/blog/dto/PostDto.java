package com.example.blog.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *  Post Dto.
 */

@Getter
@Setter
@JsonPropertyOrder({"id", "timestamp", "active", "user", "title", "announce", "text",
    "likeCount", "dislikeCount", "commentCount", "viewCount", "comments", "tags"})
public class PostDto {
  private int id;
  @JsonProperty("timestamp")
  private long time;
  private boolean active;
  @JsonProperty("user")
  @JsonIgnoreProperties({"email", "photo", "moderation", "moderationCount", "settings"})
  private UserDto userDto;
  private String title;
  private String announce;
  private String text;
  private int likeCount;
  private int dislikeCount;
  private int commentCount;
  private int viewCount;
  @JsonProperty("comments")
  private List<CommentDto> commentsDto;
  @JsonProperty("tags")
  private List<String> tagsDto;
}
