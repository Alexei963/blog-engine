package com.example.blog.controller;

import com.example.blog.api.request.ChangePasswordRequest;
import com.example.blog.api.request.LoginRequest;
import com.example.blog.api.request.RegisterRequest;
import com.example.blog.api.request.RestorePasswordRequest;
import com.example.blog.api.response.CaptchaResponse;
import com.example.blog.api.response.LoginResponse;
import com.example.blog.api.response.ResultAndErrorsResponse;
import com.example.blog.api.response.ResultResponse;
import com.example.blog.service.AuthService;
import java.io.IOException;
import java.security.Principal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
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
  private ResponseEntity<LoginResponse> checkResponse(Principal principal) {
    if (principal == null) {
      return new ResponseEntity<>(new LoginResponse(), HttpStatus.OK);
    }
    return new ResponseEntity<>(authService.getLoginResponse(principal.getName()), HttpStatus.OK);
  }

  @GetMapping("/captcha")
  private ResponseEntity<CaptchaResponse> captchaResponse() throws IOException {
    return new ResponseEntity<>(authService.getCaptchaResponse(), HttpStatus.OK);
  }

  @PostMapping("/register")
  private ResponseEntity<ResultAndErrorsResponse> registerResponse(
      @RequestBody RegisterRequest registerRequest) {
    return new ResponseEntity<>(authService.getRegisterResponse(registerRequest), HttpStatus.OK);
  }

  @PostMapping("/login")
  private ResponseEntity<LoginResponse> loginResponse(@RequestBody LoginRequest loginRequest) {
    User user = (User) authService.getAuthentication(
        loginRequest.getEmail(), loginRequest.getPassword()).getPrincipal();
    return new ResponseEntity<>(authService.getLoginResponse(user.getUsername()), HttpStatus.OK);
  }

  @GetMapping("/logout")
  private ResponseEntity<LoginResponse> logoutResponse() {
    return new ResponseEntity<>(authService.getLogout(), HttpStatus.OK);
  }

  @PostMapping("/restore")
  public ResponseEntity<ResultResponse> restorePassword(
      @RequestBody RestorePasswordRequest restorePasswordRequest) {
    return new ResponseEntity<>(authService.send(restorePasswordRequest), HttpStatus.OK);
  }

  @PostMapping("/password")
  public ResponseEntity<ResultAndErrorsResponse> changePassword(
      @RequestBody ChangePasswordRequest changePasswordRequest) {
    return new ResponseEntity<>(authService.changePassword(changePasswordRequest), HttpStatus.OK);
  }
}
