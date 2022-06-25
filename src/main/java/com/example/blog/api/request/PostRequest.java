package com.example.blog.api.request;

import com.example.blog.model.Tag;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {

  private long timestamp;
  private int active;
  private String title;
  private Set<Tag> tags;
  private String text;
}
