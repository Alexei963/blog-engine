package com.example.blog.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Register Request.
 */

@Data
public class RegisterRequest {
  @JsonProperty("e_mail")
  private String email;
  private String password;
  private String name;
  private String captcha;
  @JsonProperty("captcha_secret")
  private String captchaSecret;
}
