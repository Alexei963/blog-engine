package com.example.blog.repository;

import com.example.blog.model.Tag;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Tag Repository.
 */

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

  @Query(value = "select * "
      + "from tags t "
      + "where t.name = :name ", nativeQuery = true)
  Optional<Tag> findByName(@Param("name") String name);
}
