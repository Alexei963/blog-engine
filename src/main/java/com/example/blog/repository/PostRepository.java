package com.example.blog.repository;

import com.example.blog.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Post Repository.
 */

@Repository
public interface PostRepository extends CrudRepository<Post, Integer> {

  Page<Post> findAll(Pageable pageable);
  
  @Query(value = "select * "
      + "from posts p left outer join post_comments c "
      + "on p.id = c.post_id "
      + "group by p.id order by count(c.id) desc", nativeQuery = true)
  Page<Post> findPopularPosts(Pageable pageable);

  @Query(value = "select * from posts order by time", nativeQuery = true)
  Page<Post> findEarlyPosts(Pageable pageable);

  @Query(value = "select * "
      + "from posts p left outer join post_votes v "
      + "on p.id = v.post_id "
      + "and v.value = 1 "
      + "group by p.id order by count(v.id) desc", nativeQuery = true)
  Page<Post> findBestPosts(Pageable pageable);

  @Query(value = "select * from posts order by time desc", nativeQuery = true)
  Page<Post> findRecentPosts(Pageable pageable);
}
