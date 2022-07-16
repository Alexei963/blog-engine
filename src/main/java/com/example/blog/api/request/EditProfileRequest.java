package com.example.blog.api.request;

import lombok.Data;

/**
 * Edit Profile Request.
 */

@Data
public class EditProfileRequest {

  private String name;
  private String email;
  private String password;
  private int removePhoto;
}
