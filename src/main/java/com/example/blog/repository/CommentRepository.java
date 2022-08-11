package com.example.blog.repository;

import com.example.blog.model.PostComment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Comment Repository.
 */

@Repository
public interface CommentRepository extends CrudRepository<PostComment, Integer> {

  @Query(value = "select * "
      + "from post_comments ps "
      + "where ps.id = :id ", nativeQuery = true)
  Optional<PostComment> findById(@Param("id") int id);

  @Query(value = "select * "
      + "from post_comments ps "
      + "where ps.parent_id = :id ", nativeQuery = true)
  List<PostComment> findByParentId(@Param("id") int id);
}
