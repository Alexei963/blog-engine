package com.example.blog.repository;

import com.example.blog.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * User Repository.
 */

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

}
