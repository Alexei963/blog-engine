package com.example.blog.api.response;

import java.util.ArrayList;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * Tag Response.
 */

@Getter
@Setter
public class TagResponse {

  private ArrayList<Map<String, Object>> tags = new ArrayList<>();
}
