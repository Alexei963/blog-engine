package com.example.blog.service;

import com.example.blog.model.User;
import com.example.blog.repository.UserRepository;
import java.util.Optional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * User Service.
 */

@Service
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User getLoggedUser() {
    String loggedUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
    User user = null;
    Optional<User> optionalUser = userRepository.findByEmail(loggedUserEmail);
    if (optionalUser.isPresent()) {
      user = optionalUser.get();
    }
    return user;
  }
}
