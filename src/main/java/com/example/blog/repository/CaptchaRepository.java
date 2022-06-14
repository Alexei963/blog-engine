package com.example.blog.repository;

import com.example.blog.model.Captcha;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Captcha Repository.
 */

@Repository
public interface CaptchaRepository extends CrudRepository<Captcha, Integer> {
  Optional<Captcha> findByCode(String code);
}
