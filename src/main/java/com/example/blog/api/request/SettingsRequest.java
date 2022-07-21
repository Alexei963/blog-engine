package com.example.blog.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Settings Request.
 */

@Data
public class SettingsRequest {

  @JsonProperty("MULTIUSER_MODE")
  private boolean multiUserMode;
  @JsonProperty("POST_PREMODERATION")
  private boolean postPreModeration;
  @JsonProperty("STATISTICS_IS_PUBLIC")
  private boolean statisticsIsPublic;
}
