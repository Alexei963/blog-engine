package com.example.blog.repository;

import com.example.blog.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends CrudRepository<Post, Integer> {

  Page<Post> findAll(Pageable pageable);
  
  @Query(value = "select p.*, count(c.id) count "
      + "from posts p left outer join post_comments c "
      + "on p.id = c.post_id "
      + "group by post_id order by count desc", nativeQuery = true)
  Page<Post> findPopularPosts(Pageable pageable);

  @Query(value = "select * from posts order by time", nativeQuery = true)
  Page<Post> findEarlyPosts(Pageable pageable);

  @Query(value = "select posts.* "
      + "from posts left outer join post_votes "
      + "on posts.id = post_votes.post_id "
      + "and post_votes.value = 1 "
      + "group by post_id, id order by count(post_votes.id) desc", nativeQuery = true)
  Page<Post> findBestPosts(Pageable pageable);

  @Query(value = "select * from posts order by time desc", nativeQuery = true)
  Page<Post> findRecentPosts(Pageable pageable);
}
