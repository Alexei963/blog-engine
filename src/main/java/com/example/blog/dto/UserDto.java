package com.example.blog.dto;

import lombok.Getter;
import lombok.Setter;

/**
 *  Comment Dto.
 */

@Getter
@Setter
public class UserDto {
  private int id;
  private String name;
  private String photo;
  private String email;
  private boolean moderation;
  private int moderationCount;
  private boolean settings;
}
