package com.example.blog.service;

import com.example.blog.api.request.RegisterRequest;
import com.example.blog.api.response.CaptchaResponse;
import com.example.blog.api.response.CheckResponse;
import com.example.blog.api.response.RegisterResponse;
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
import javax.imageio.ImageIO;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

/**
 * Check Service.
 */

@Service
public class AuthService {

  private final CaptchaRepository captchaRepository;
  private final UserRepository userRepository;

  public AuthService(CaptchaRepository captchaRepository,
      UserRepository userRepository) {
    this.captchaRepository = captchaRepository;
    this.userRepository = userRepository;
  }


  public CheckResponse getCheckResponse() {
    CheckResponse checkResponse = new CheckResponse();
    checkResponse.setResult(false);
    return checkResponse;
  }

  public CaptchaResponse getCaptchaResponse() throws IOException {
    GCage captchaGenerator = new GCage();
    String token = captchaGenerator.getTokenGenerator().next();
    Captcha captcha = new Captcha();
    captcha.setTime(new Date());
    captcha.setCode(token);
    captcha.setSecretCode(token);
    captchaRepository.save(captcha);
    BufferedImage bufferedImage = captchaGenerator.drawImage(token);
    File image = new File("image.jpeg");
    ImageIO.write(bufferedImage, "jpeg", image);
    byte[] fileContent = FileUtils.readFileToByteArray(image);
    String encodedString = Base64.getEncoder().encodeToString(fileContent);
    String header = "data:image/png;base64, ";
    CaptchaResponse captchaResponse = new CaptchaResponse();
    captchaResponse.setSecret(token);
    captchaResponse.setImage(header.concat(encodedString));
    return captchaResponse;
  }

  public RegisterResponse getRegisterResponse(RegisterRequest registerRequest) {
    RegisterResponse registerResponse = new RegisterResponse();
    Optional<Captcha> captcha = captchaRepository.findByCode(registerRequest.getCaptcha());
    boolean userEmail = userRepository.findByEmail(registerRequest.getEmail()).isPresent();
    Map<String, String> errorsMap = new LinkedHashMap<>();
    if (captcha.isPresent() && !userEmail
        && captcha.get().getCode().equals(registerRequest.getCaptcha())) {
      User user = new User();
      user.setRegTime(new Date());
      user.setName(registerRequest.getName());
      user.setEmail(registerRequest.getEmail());
      user.setPassword(registerRequest.getPassword());
      userRepository.save(user);
      registerResponse.setResult(true);
    }
    if (userEmail) {
      errorsMap.put("email", "Этот e-mail уже зарегистрирован");
    }
    if (registerRequest.getPassword().length() < 6) {
      errorsMap.put("password", "Пароль короче 6-ти символов");
    }
    if (captcha.isEmpty()) {
      errorsMap.put("captcha", "Код с картинки введён неверно");
    }
    registerResponse.setErrors(errorsMap);
    return registerResponse;
  }
}
