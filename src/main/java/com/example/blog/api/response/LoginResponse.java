package com.example.blog.api.response;

import com.example.blog.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class LoginResponse {

  private boolean result;
  @JsonProperty("user")
  private UserDto userDto;
}
