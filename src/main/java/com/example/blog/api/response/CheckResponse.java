package com.example.blog.api.response;

import com.example.blog.model.User;
import lombok.Getter;
import lombok.Setter;

/**
 * Check Response.
 */

@Getter
@Setter
public class CheckResponse {

  private boolean result;
  private User user;
}
