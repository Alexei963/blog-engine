package com.example.blog.api.response;

import lombok.Data;

/**
 * Captcha response.
 */

@Data
public class CaptchaResponse {
  private String secret;
  private String image;
}
