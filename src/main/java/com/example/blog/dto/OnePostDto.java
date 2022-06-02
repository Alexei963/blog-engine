package com.example.blog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OnePostDto {
  private int id;
  @JsonProperty("timestamp")
  private long time;
  private boolean active;
  @JsonProperty("user")
  private UserDto userDto;
  private String title;
  private String text;
  private int likeCount;
  private int dislikeCount;
  private int viewCount;
  @JsonProperty("comments")
  private List<CommentDto> commentsDto;
  @JsonProperty("tags")
  private Set<String> tagsDto = new HashSet<>();
}
