package com.example.blog.api.response;

import java.util.ArrayList;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * Post Response.
 */

@Getter
@Setter
public class PostResponse {

  private int count;
  private ArrayList<Map<String, Object>> posts = new ArrayList<>();
}
