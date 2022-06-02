package com.example.blog.repository;

import com.example.blog.model.Post;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Post Repository.
 */

@Repository
public interface PostRepository extends CrudRepository<Post, Integer> {

  Page<Post> findAll(Pageable pageable);

  @Query(value = "select * "
      + "from posts p "
      + "where p.id = :id "
      + "and p.is_active = 1 and p.moderation_status = 'ACCEPTED' "
      + "and date(time) <= now()", nativeQuery = true)
  Optional<Post> findById(@Param("id") int id);

  @Query(value = "select * "
      + "from posts p "
      + "where p.text like %:query% "
      + "and p.is_active = 1 and p.moderation_status = 'ACCEPTED' "
      + "and date(time) <= now()", nativeQuery = true)
  Page<Post> postsSearch(@Param("query") String query, Pageable pageable);
  
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

  @Query(value = "select * "
      + "from posts p "
      + "where p.time like %:date% "
      + "and p.is_active = 1 and p.moderation_status = 'ACCEPTED' "
      + "and date(time) <= now() "
      + "order by time desc", nativeQuery = true)
  Page<Post> findPostsByDate(@Param("date") String date, Pageable pageable);

  @Query(value = "select * "
      + "from posts p "
      + "where p.time like %:date% "
      + "and p.is_active = 1 and p.moderation_status = 'ACCEPTED' "
      + "and date(time) <= now() "
      + "group by cast(time as Date) "
      + "order by time desc", nativeQuery = true)
  List<Post> findPostsByDate(@Param("date") String date);

  @Query(value = "select count(*) "
      + "from posts p "
      + "where p.time like %:date% "
      + "and p.is_active = 1 and p.moderation_status = 'ACCEPTED' "
      + "and date(time) <= now() "
      + "order by p.time desc", nativeQuery = true)
  int countPostsByTime(@Param("date") String date);

  @Query(value = "select * "
      + "from posts p "
      + "join tag2post tp on p.id = tp.post_id "
      + "join tags t on tp.tag_id = t.id "
      + "where t.name = :tag "
      + "and p.is_active = 1 and p.moderation_status = 'ACCEPTED' "
      + "and date(time) <= now()", nativeQuery = true)
  Page<Post> findPostsByTags(String tag, Pageable pageable);
}
