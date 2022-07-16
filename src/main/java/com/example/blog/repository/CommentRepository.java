package com.example.blog.repository;

import com.example.blog.model.PostComment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Comment Repository.
 */

@Repository
public interface CommentRepository extends CrudRepository<PostComment, Integer> {

}
