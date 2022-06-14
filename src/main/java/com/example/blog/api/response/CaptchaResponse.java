package com.example.blog.api.response;

import lombok.Getter;
import lombok.Setter;

/**
 * Captcha response.
 */

@Getter
@Setter
public class CaptchaResponse {
  private String secret;
  private String image;
}
