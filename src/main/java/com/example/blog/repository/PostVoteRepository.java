package com.example.blog.repository;

import com.example.blog.model.PostVote;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Post Vote Repository.
 */

@Repository
public interface PostVoteRepository extends CrudRepository<PostVote, Integer> {

  @Query(value = "select * "
      + "from post_votes pv "
      + "where pv.user_id = :userId "
      + "and pv.post_id = :postId ", nativeQuery = true)
  Optional<PostVote> findPostVoteByUserId(@Param("userId") int userId, @Param("postId") int postId);
}
