package com.example.blog.api.response;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * Calendar Response.
 */

@Getter
@Setter
public class CalendarResponse {
  private List<Integer> years = new ArrayList<>();
  private Map<Object, Integer> posts = new LinkedHashMap<>();
}
