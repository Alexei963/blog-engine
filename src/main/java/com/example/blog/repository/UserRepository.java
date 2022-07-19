package com.example.blog.repository;

import com.example.blog.model.User;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * User Repository.
 */

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

  Optional<User> findByEmail(String email);

  Optional<User> findByCode(String code);
}
