package com.example.blog.repository;

import com.example.blog.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Tag Repository.
 */

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

  Tag findByName(String name);
}
