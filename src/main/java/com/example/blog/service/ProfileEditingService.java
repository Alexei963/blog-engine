package com.example.blog.service;

import com.example.blog.api.request.EditProfileRequest;
import com.example.blog.api.response.ResultAndErrorsResponse;
import com.example.blog.model.User;
import com.example.blog.repository.UserRepository;
import java.io.IOException;
import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProfileEditingService {

  private final UserRepository userRepository;
  private final ImageService imageService;
  private final PasswordEncoder passwordEncoder;

  private static final int PHOTO_WIDTH = 150;
  private static final int MAX_IMAGE_SIZE = 5242880; // 5 мегабайт

  public ProfileEditingService(UserRepository userRepository,
      ImageService imageService,
      PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.imageService = imageService;
    this.passwordEncoder = passwordEncoder;
  }

  public ResultAndErrorsResponse profileAndPhotoEditing(MultipartFile photo, String name,
      String email, String password, String path, Principal principal) throws IOException {
    ResultAndErrorsResponse response = new ResultAndErrorsResponse();
    User user = getUser(principal.getName());
    if (photo != null && photo.getSize() < MAX_IMAGE_SIZE) {
      user.setPhoto(imageService.getImagePath(photo, path, PHOTO_WIDTH));
      response.setResult(true);
    }
    if (name != null && email != null && password != null) {
      editProfileWithoutPhoto(name, email, password, principal);
    }
    userRepository.save(user);
    return response;
  }

  public ResultAndErrorsResponse deletePhoto(Principal principal,
      EditProfileRequest editProfileRequest) {
    ResultAndErrorsResponse response = new ResultAndErrorsResponse();
    User user = getUser(principal.getName());
    if (editProfileRequest.getRemovePhoto() == 1) {
      user.setPhoto(null);
      userRepository.save(user);
      response.setResult(true);
    }
    return response;
  }

  public ResultAndErrorsResponse editProfileWithoutPhoto(String name, String email,
      String password, Principal principal) {
    ResultAndErrorsResponse response = new ResultAndErrorsResponse();
    User user = getUser(principal.getName());
    boolean userEmail = userRepository.findByEmail(email).isPresent();
    String regexName = "[А-Яа-яA-Za-z]+([А-Яа-яA-Za-z\\s]+)?";
    if (!user.getName().equals(name) && name.matches(regexName)) {
      user.setName(name);
      response.setResult(true);
    }
    if (!user.getEmail().equals(email) || !userEmail) {
      user.setEmail(email);
      response.setResult(true);
    }
    if (password != null && !user.getPassword().equals(password)) {
      user.setPassword(passwordEncoder.encode(password));
      response.setResult(true);
    }
    userRepository.save(user);
    return response;
  }

  private User getUser(String email) {
    Optional<User> userOptional = userRepository.findByEmail(email);
    User user = null;
    if (userOptional.isPresent()) {
      user = userOptional.get();
    }
    return user;
  }

  public ResultAndErrorsResponse imageChangeErrors(MultipartFile photo) {
    ResultAndErrorsResponse response = new ResultAndErrorsResponse();
    Map<String, String> errors = new LinkedHashMap<>();
    if (photo != null && photo.getSize() > MAX_IMAGE_SIZE) {
      errors.put("image size", "Размер файла превышает допустимый размер");
    }
    response.setErrors(errors);
    return response;
  }

  public ResultAndErrorsResponse profileChangeErrors(String name, String email,
      String password) {
    boolean userEmail = userRepository.findByEmail(email).isPresent();
    Map<String, String> errors = new LinkedHashMap<>();
    String regexName = "[А-Яа-яA-Za-z]+([А-Яа-яA-Za-z\\s]+)?";

    if (userEmail) {
      errors.put("email", "Этот e-mail уже зарегистрирован");
    }
    if (name != null && !name.matches(regexName)) {
      errors.put("name", "Имя указано неверно");
    }
    if (password != null && password.length() < 6) {
      errors.put("password", "Пароль короче 6-ти символов");
    }
    ResultAndErrorsResponse response = new ResultAndErrorsResponse();
    response.setErrors(errors);
    return response;
  }
}
