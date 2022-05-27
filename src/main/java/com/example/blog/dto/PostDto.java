package com.example.blog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostDto {

  private int id;
  @JsonProperty("timestamp")
  private long time;
  @JsonProperty("user")
  private UserDto userDto;
  private String title;
  private String announce;
  private int likeCount;
  private int dislikeCount;
  private int commentCount;
  private int viewCount;
}
