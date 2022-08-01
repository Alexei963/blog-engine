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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Auth Service.
 */

@Service
@EnableScheduling
public class AuthService {

  static final Logger logger = LoggerFactory.getLogger(AuthService.class);

  private final CaptchaRepository captchaRepository;
  private final UserRepository userRepository;
  private final MapperService mapperService;
  private final AuthenticationManager authenticationManager;
  private final PasswordEncoder passwordEncoder;
  private final JavaMailSender mailSender;
  private final UserService userService;

  @Value("{spring.mail.username}")
  private String username;
  private static final int CAPTCHA_CLEAR_TIME = 3_600_000;

  public AuthService(CaptchaRepository captchaRepository,
      UserRepository userRepository, MapperService mapperService,
      AuthenticationManager authenticationManager,
      PasswordEncoder passwordEncoder, JavaMailSender mailSender,
      UserService userService) {
    this.captchaRepository = captchaRepository;
    this.userRepository = userRepository;
    this.mapperService = mapperService;
    this.authenticationManager = authenticationManager;
    this.passwordEncoder = passwordEncoder;
    this.mailSender = mailSender;
    this.userService = userService;
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
      logger.info("Зарегистрировался новый пользователь {}", user.getName());
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

  public LoginResponse getCheckResponse() {
    User user = userService.getLoggedUser();
    return getLoginResponse(user);
  }

  public LoginResponse login(String email, String password) {
    Optional<User> optionalUser = userRepository.findByEmail(email);
    if (optionalUser.isPresent()) {
      User currentUser = optionalUser.get();
      Authentication auth = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(email, password));
      SecurityContextHolder.getContext().setAuthentication(auth);
      logger.info("Пользователь {} авторизовался.", currentUser.getName());
      return getLoginResponse(currentUser);
    }
    return new LoginResponse();
  }

  private LoginResponse getLoginResponse(User user) {
    LoginResponse loginResponse = new LoginResponse();
    if (user != null) {
      UserDto userDto = mapperService.convertUserToDto(user);
      loginResponse.setResult(true);
      loginResponse.setUserDto(userDto);
    }
    return loginResponse;
  }

  public LoginResponse logout() {
    LoginResponse loginResponse = new LoginResponse();
    loginResponse.setResult(true);
    logger.info("Пользователь {} вышел.", userService.getLoggedUser().getName());
    SecurityContextHolder.getContext().setAuthentication(null);
    SecurityContextHolder.clearContext();
    return loginResponse;
  }

  public ResultResponse sendPasswordChangeLink(RestorePasswordRequest restorePasswordRequest) {
    ResultResponse response = new ResultResponse();
    Optional<User> optionalUser = userRepository.findByEmail(restorePasswordRequest.getEmail());
    User user;
    if (optionalUser.isPresent()) {
      user = optionalUser.get();
      String code = UUID.randomUUID().toString();
      user.setCode(code);
      userRepository.save(user);
      String baseUrl = ServletUriComponentsBuilder
          .fromCurrentContextPath().build().toUriString();
      String link = baseUrl + "/login/change-password/" + code;
      String passwordRecoveryLink = String.format("Добрый день %s. \n"
              + "Чтобы изменить пароль перейдите по ссылке: %s",
          user.getName(), link);
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
      logger.info("Пользователь {} изменил пароль.", user.getName());
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
