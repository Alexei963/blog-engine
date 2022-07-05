package com.example.blog.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 *  Post response.
 */

@Getter
@Setter
@JsonInclude(Include.NON_EMPTY)
public class ResultAndErrorsResponse {

  private boolean result;
  private Map<String, String> errors;
}
