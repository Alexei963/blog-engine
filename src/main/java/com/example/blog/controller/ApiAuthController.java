package com.example.blog.controller;


import com.example.blog.api.response.CheckResponse;
import com.example.blog.service.AuthCheckService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Api Auth Controller.
 */

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

  private final AuthCheckService checkService;

  public ApiAuthController(AuthCheckService checkService) {
    this.checkService = checkService;
  }

  @GetMapping("/check")
  private ResponseEntity<CheckResponse> checkResponse() {
    return new ResponseEntity<>(checkService.getCheckResponse(), HttpStatus.OK);
  }
}
