package com.example.blog.model;

import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity "PostVote" from table post_votes.
 */

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "post_votes")
public class PostVote {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "user_id")
  private User user;
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "post_id")
  private Post post;
  private Date time;
  private int value;
}
