package com.example.blog.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Change Password Request.
 */

@Data
public class ChangePasswordRequest {

  private String code;
  private String password;
  private String captcha;
  @JsonProperty("captcha_secret")
  private String captchaSecret;
}
