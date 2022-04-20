package com.example.blogEngine.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "post_votes")
public class LikeAndDislikePost {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private int userId;
  private int postId;
  private java.sql.Timestamp time;
  private int value;
}
