package com.example.blogEngine.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "post_comments")
public class CommentOnPost {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private int parentId;
  @ManyToOne(cascade = CascadeType.ALL)
  private Post post;
  @ManyToOne(cascade = CascadeType.ALL)
  private User user;
  private java.sql.Timestamp time;
  private String text;
}
