package com.example.blog.api.response;

import java.util.Map;
import lombok.Data;

/**
 * Register response.
 */

@Data
public class RegisterResponse {
  private boolean result;
  private Map<String, String> errors;
}
