package com.example.blog.service;

import com.example.blog.api.request.ChangePasswordRequest;
import com.example.blog.api.request.RegisterRequest;
import com.example.blog.api.request.RestorePasswordRequest;
import com.example.blog.api.response.CaptchaResponse;
import com.example.blog.api.response.LoginResponse;
import com.example.blog.api.response.ResultAndErrorsResponse;
import com.example.blog.api.response.ResultResponse;
import com.example.blog.dto.UserDto;
import com.example.blog.model.Captcha;
import com.example.blog.model.User;
import com.example.blog.repository.CaptchaRepository;
import com.example.blog.repository.UserRepository;
import com.github.cage.GCage;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import javax.imageio.ImageIO;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Auth Service.
 */

@Service
@EnableScheduling
public class AuthService {

  private final CaptchaRepository captchaRepository;
  private final UserRepository userRepository;
  private final MapperService mapperService;
  private final AuthenticationManager authenticationManager;
  private final PasswordEncoder passwordEncoder;
  private final JavaMailSender mailSender;

  @Value("{spring.mail.username}")
  private String username;
  private static final int CAPTCHA_CLEAR_TIME = 3_600_000;

  public AuthService(CaptchaRepository captchaRepository,
      UserRepository userRepository, MapperService mapperService,
      AuthenticationManager authenticationManager,
      PasswordEncoder passwordEncoder, JavaMailSender mailSender) {
    this.captchaRepository = captchaRepository;
    this.userRepository = userRepository;
    this.mapperService = mapperService;
    this.authenticationManager = authenticationManager;
    this.passwordEncoder = passwordEncoder;
    this.mailSender = mailSender;
  }

  public CaptchaResponse getCaptchaResponse() throws IOException {
    GCage captchaGenerator = new GCage();
    String token = captchaGenerator.getTokenGenerator().next();
    String secret = UUID.randomUUID().toString();
    Captcha captcha = new Captcha();
    captcha.setTime(new Date());
    captcha.setCode(token);
    captcha.setSecretCode(secret);
    captchaRepository.save(captcha);
    BufferedImage bufferedImage = captchaGenerator.drawImage(token);
    File image = new File("image.jpeg");
    ImageIO.write(bufferedImage, "jpeg", image);
    byte[] fileContent = FileUtils.readFileToByteArray(image);
    String encodedString = Base64.getEncoder().encodeToString(fileContent);
    String header = "data:image/png;base64, ";
    CaptchaResponse captchaResponse = new CaptchaResponse();
    captchaResponse.setSecret(secret);
    captchaResponse.setImage(header.concat(encodedString));
    return captchaResponse;
  }

  public ResultAndErrorsResponse getRegisterResponse(RegisterRequest registerRequest) {
    ResultAndErrorsResponse registerResponse = new ResultAndErrorsResponse();
    Captcha captcha = captchaRepository.findBySecretCode(registerRequest.getCaptchaSecret());
    boolean userEmail = userRepository.findByEmail(registerRequest.getEmail()).isPresent();
    String regexName = "[А-Яа-яA-Za-z]+([А-Яа-яA-Za-z\\s]+)?";
    boolean compareCaptcha = captcha.getCode().equals(registerRequest.getCaptcha());
    int passwordLength = registerRequest.getPassword().length();
    Map<String, String> errorsMap = new LinkedHashMap<>();
    if (!userEmail && compareCaptcha
        && registerRequest.getName().matches(regexName)
        && passwordLength >= 6) {
      User user = new User();
      user.setRegTime(new Date());
      user.setName(registerRequest.getName());
      user.setEmail(registerRequest.getEmail());
      user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
      userRepository.save(user);
      registerResponse.setResult(true);
    }
    if (userEmail) {
      errorsMap.put("email", "Этот e-mail уже зарегистрирован");
    }
    if (!registerRequest.getName().matches(regexName)) {
      errorsMap.put("name", "Имя указано неверно");
    }
    if (passwordLength < 6) {
      errorsMap.put("password", "Пароль короче 6-ти символов");
    }
    if (!compareCaptcha) {
      errorsMap.put("captcha", "Код с картинки введён неверно");
    }
    registerResponse.setErrors(errorsMap);
    return registerResponse;
  }

  @Scheduled(fixedRate = CAPTCHA_CLEAR_TIME)
  public void deleteCaptcha() {
    captchaRepository.deleteAll();
  }

  public LoginResponse getLoginResponse(String email) {
    com.example.blog.model.User currentUser = userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException(email));
    UserDto userDto = mapperService.convertUserToDto(currentUser);
    LoginResponse loginResponse = new LoginResponse();
    loginResponse.setResult(true);
    loginResponse.setUserDto(userDto);
    return loginResponse;
  }

  public Authentication getAuthentication(String email, String password) {
    Authentication auth = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(email, password));
    SecurityContextHolder.getContext().setAuthentication(auth);
    return auth;
  }

  public LoginResponse getLogout() {
    LoginResponse loginResponse = new LoginResponse();
    loginResponse.setResult(true);
    SecurityContextHolder.getContext().setAuthentication(null);
    SecurityContextHolder.clearContext();
    return loginResponse;
  }

  public ResultResponse send(RestorePasswordRequest restorePasswordRequest) {
    ResultResponse response = new ResultResponse();
    Optional<User> optionalUser = userRepository.findByEmail(restorePasswordRequest.getEmail());
    User user;
    if (optionalUser.isPresent()) {
      user = optionalUser.get();
      String code = UUID.randomUUID().toString();
      user.setCode(code);
      userRepository.save(user);
      String link = "http://localhost:8080/login/change-password/";
      String passwordRecoveryLink = String.format("Добрый день %s. \n"
              + "Чтобы изменить пароль перейдите по ссылке: " + link.concat("%s"),
          user.getName(), user.getCode());
      SimpleMailMessage mailMessage = new SimpleMailMessage();
      mailMessage.setFrom(username);
      mailMessage.setTo(restorePasswordRequest.getEmail());
      mailMessage.setSubject("Изменить пароль");
      mailMessage.setText(passwordRecoveryLink);
      mailSender.send(mailMessage);
      response.setResult(true);
    }
    return response;
  }

  public ResultAndErrorsResponse changePassword(ChangePasswordRequest changePasswordRequest) {
    ResultAndErrorsResponse response = new ResultAndErrorsResponse();
    Optional<User> optionalUser = userRepository.findByCode(changePasswordRequest.getCode());
    Captcha captcha = captchaRepository.findBySecretCode(changePasswordRequest.getCaptchaSecret());
    boolean compareCaptcha = captcha.getCode().equals(changePasswordRequest.getCaptcha());
    User user = null;
    Map<String, String> errorsMap = new LinkedHashMap<>();
    if (optionalUser.isPresent()) {
      user = optionalUser.get();
    }
    int passwordLength = changePasswordRequest.getPassword().length();
    if (passwordLength >= 6 && compareCaptcha) {
      assert user != null;
      user.setPassword(passwordEncoder.encode(changePasswordRequest.getPassword()));
      userRepository.save(user);
      response.setResult(true);
    }
    if (passwordLength < 6) {
      errorsMap.put("password", "Пароль короче 6-ти символов");
    }
    if (!compareCaptcha) {
      errorsMap.put("captcha", "Код с картинки введён неверно");
    }
    response.setErrors(errorsMap);
    return response;
  }
}
