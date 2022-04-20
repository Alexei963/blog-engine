package com.example.blogEngine.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private int isActive;
  @Enumerated(EnumType.STRING)
  @Column(columnDefinition = "enum")
  private ModerationStatus moderationStatus;
  private int moderatorId;
  @ManyToOne(cascade = CascadeType.ALL)
  private User user;
  private java.sql.Timestamp time;
  private String title;
  private String text;
  private int viewCount;

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(name = "tag2post", joinColumns = @JoinColumn(name = "post_id"),
          inverseJoinColumns = @JoinColumn(name = "tag_id"))
  private List<Tag> tags;
}
