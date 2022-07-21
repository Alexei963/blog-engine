package com.example.blog.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Map;
import lombok.Data;

/**
 *  Post response.
 */

@Data
@JsonInclude(Include.NON_EMPTY)
public class ResultAndErrorsResponse {

  private boolean result;
  private Map<String, String> errors;
}
