package com.example.blog.api.request;

import java.util.List;
import lombok.Data;

/**
 * Post Request.
 */

@Data
public class PostRequest {

  private long timestamp;
  private int active;
  private String title;
  private List<String> tags;
  private String text;
}
