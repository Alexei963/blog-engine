package com.example.blog.controller;


import com.example.blog.api.request.RegisterRequest;
import com.example.blog.api.response.CaptchaResponse;
import com.example.blog.api.response.CheckResponse;
import com.example.blog.api.response.RegisterResponse;
import com.example.blog.service.AuthService;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Api Auth Controller.
 */

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

  private final AuthService authService;

  public ApiAuthController(AuthService checkService) {
    this.authService = checkService;
  }

  @GetMapping("/check")
  private ResponseEntity<CheckResponse> checkResponse() {
    return new ResponseEntity<>(authService.getCheckResponse(), HttpStatus.OK);
  }

  @GetMapping("/captcha")
  private ResponseEntity<CaptchaResponse> captchaResponse() throws IOException {
    return new ResponseEntity<>(authService.getCaptchaResponse(), HttpStatus.OK);
  }

  @PostMapping("/register")
  private ResponseEntity<RegisterResponse> registerResponse(
      @RequestBody RegisterRequest registerRequest) {
    return new ResponseEntity<>(authService.getRegisterResponse(registerRequest), HttpStatus.OK);
  }
}
