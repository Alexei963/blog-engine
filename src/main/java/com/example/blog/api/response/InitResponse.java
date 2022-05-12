package com.example.blog.api.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * Init Response.
 */

@Getter
@Setter
@Component
public class InitResponse {

  private String title;
  private String subtitle;
  private String phone;
  private String email;
  private String copyright;
  private String copyrightFrom;
}
