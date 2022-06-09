package com.example.blog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {
  private int id;
  @JsonProperty("timestamp")
  private long time;
  private String text;
  @JsonProperty("user")
  private UserDto userDto;
}
