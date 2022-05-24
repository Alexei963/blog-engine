package com.example.blog.model;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity "Post" from table posts.
 */

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @Column(name = "is_active", columnDefinition = "TINYINT", nullable = false)
  private int isActive;
  @Enumerated(EnumType.STRING)
  @Column(name = "moderation_status",
      columnDefinition = "enum('NEW', 'ACCEPTED', 'DECLINED') default 'NEW'",
      nullable = false)
  private ModerationStatus moderationStatus;
  @Column(name = "moderator_id")
  private int moderatorId;
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "user_id")
  private User user;
  private Date time;
  private String title;
  @Column(columnDefinition = "TEXT", nullable = false)
  private String text;
  @Column(name = "view_count", nullable = false)
  private int viewCount;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
  private List<PostVote> votes;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
  private List<PostComment> comments;

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(name = "tag2post", joinColumns = @JoinColumn(name = "post_id"),
      inverseJoinColumns = @JoinColumn(name = "tag_id"))
  private Set<Tag> tags = new HashSet<>();
}
