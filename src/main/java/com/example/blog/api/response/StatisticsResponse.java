package com.example.blog.api.response;

import lombok.Data;

/**
 * My Statistics Response.
 */

@Data
public class StatisticsResponse {

  private int postsCount;
  private int likesCount;
  private int dislikesCount;
  private int viewsCount;
  private long firstPublication;
}
