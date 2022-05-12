package com.example.blog.service;

import com.example.blog.api.response.CheckResponse;
import org.springframework.stereotype.Service;

/**
 * Check Service.
 */

@Service
public class AuthCheckService {
  public CheckResponse getCheckResponse() {
    CheckResponse checkResponse = new CheckResponse();
    checkResponse.setResult(false);
    return checkResponse;
  }
}
