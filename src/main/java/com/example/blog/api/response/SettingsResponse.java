package com.example.blog.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Settings Response.
 */

@Data
public class SettingsResponse {

  @JsonProperty("MULTIUSER_MODE")
  private boolean multiUserMode;
  @JsonProperty("POST_PREMODERATION")
  private boolean postPreModeration;
  @JsonProperty("STATISTICS_IS_PUBLIC")
  private boolean statisticsIsPublic;
}
