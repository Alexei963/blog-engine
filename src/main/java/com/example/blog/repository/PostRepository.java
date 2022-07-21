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
      + "and p.time <= now()", nativeQuery = true)
  Optional<Post> findById(@Param("id") int id);

  @Query(value = "select * "
      + "from posts p "
      + "where p.title like %:query% "
      + "and p.is_active = 1 and p.moderation_status = 'ACCEPTED' "
      + "and p.time <= now()", nativeQuery = true)
  Page<Post> postsSearch(@Param("query") String query, Pageable pageable);
  
  @Query(value = "select * "
      + "from posts p "
      + "left outer join post_comments c "
      + "on p.id = c.post_id "
      + "where p.is_active = 1 and p.moderation_status = 'ACCEPTED' "
      + "group by p.id order by count(c.id) desc", nativeQuery = true)
  Page<Post> findPopularPosts(Pageable pageable);

  @Query(value = "select * "
      + "from posts p "
      + "where p.is_active = 1 and p.moderation_status = 'ACCEPTED' "
      + "order by time", nativeQuery = true)
  Page<Post> findEarlyPosts(Pageable pageable);

  @Query(value = "select * "
      + "from posts p "
      + "left outer join post_votes v "
      + "on p.id = v.post_id "
      + "where p.is_active = 1 and p.moderation_status = 'ACCEPTED' "
      + "and v.value = 1 "
      + "group by p.id order by count(v.id) desc", nativeQuery = true)
  Page<Post> findBestPosts(Pageable pageable);

  @Query(value = "select * "
      + "from posts p "
      + "where p.is_active = 1 and p.moderation_status = 'ACCEPTED' "
      + "order by time desc", nativeQuery = true)
  Page<Post> findRecentPosts(Pageable pageable);

  @Query(value = "select distinct year(time) "
      + "year from posts "
      + "order by year asc", nativeQuery = true)
  List<Integer> findYearsPosts();

  @Query(value = "select * "
      + "from posts p "
      + "where date_format(p.time, '%Y-%m-%d') = :date "
      + "and p.is_active = 1 and p.moderation_status = 'ACCEPTED' "
      + "and p.time <= now() "
      + "order by time desc", nativeQuery = true)
  Page<Post> findPostsByDate(@Param("date") String date, Pageable pageable);

  @Query(value = "select * "
      + "from posts p "
      + "where year(p.time) = :date "
      + "and p.is_active = 1 and p.moderation_status = 'ACCEPTED' "
      + "and p.time <= now() "
      + "order by time desc", nativeQuery = true)
  List<Post> findPostsByDate(@Param("date") String date);

  @Query(value = "select * "
      + "from posts p "
      + "join tag2post tp on p.id = tp.post_id "
      + "join tags t on tp.tag_id = t.id "
      + "where t.name = :tag "
      + "and p.is_active = 1 and p.moderation_status = 'ACCEPTED' "
      + "and date(time) <= now()", nativeQuery = true)
  Page<Post> findPostsByTags(String tag, Pageable pageable);

  @Query(value = "select count(p.id) "
      + "from posts p "
      + "where p.moderation_status = 'NEW' ", nativeQuery = true)
  int countByModerationStatus();

  @Query(value = "select * "
      + "from posts p "
      + "join users u on p.user_id = u.id "
      + "where u.email = :email "
      + "and p.is_active = 0", nativeQuery = true)
  Page<Post> findMyInactivePosts(@Param("email") String email, Pageable pageable);

  @Query(value = "select * "
      + "from posts p "
      + "join users u on p.user_id = u.id "
      + "where u.email = :email "
      + "and p.is_active = 1 and p.moderation_status = 'NEW'", nativeQuery = true)
  Page<Post> findMyPendingPosts(@Param("email") String email, Pageable pageable);

  @Query(value = "select * "
      + "from posts p "
      + "join users u on p.user_id = u.id "
      + "where u.email = :email "
      + "and p.is_active = 1 and p.moderation_status = 'DECLINED'", nativeQuery = true)
  Page<Post> findMyDeclinedPosts(@Param("email") String email, Pageable pageable);

  @Query(value = "select * "
      + "from posts p "
      + "join users u on p.user_id = u.id "
      + "where u.email = :email "
      + "and p.is_active = 1 and p.moderation_status = 'ACCEPTED'", nativeQuery = true)
  Page<Post> findMyPublishedPosts(@Param("email") String email, Pageable pageable);

  @Query(value = "select * "
      + "from posts p "
      + "where p.is_active = 1 and p.moderation_status = 'NEW'", nativeQuery = true)
  Page<Post> findPostsByModerationStatus_New(Pageable pageable);

  @Query(value = "select * "
      + "from posts p "
      + "where p.is_active = 1 and p.moderation_status = 'DECLINED'", nativeQuery = true)
  Page<Post> findPostsByModerationStatus_Declined(Pageable pageable);

  @Query(value = "select * "
      + "from posts p "
      + "where p.is_active = 1 and p.moderation_status = 'ACCEPTED'", nativeQuery = true)
  Page<Post> findPostsByModerationStatus_Accepted(Pageable pageable);

  @Query(value = "select * "
      + "from posts p "
      + "where p.is_active = 1 and p.moderation_status = 'ACCEPTED'", nativeQuery = true)
  List<Post> allAcceptedPosts();
}
