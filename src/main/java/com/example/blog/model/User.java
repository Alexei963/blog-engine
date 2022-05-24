package com.example.blog.model;

import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity "User" from table users.
 */

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @Column(name = "is_moderator")
  private int isModerator;
  @Column(name = "reg_time")
  private Date regTime;
  private String name;
  private String email;
  private String password;
  private String code;
  private String photo;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
  private List<Post> posts;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
  private List<PostVote> votes;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
  private List<PostComment> comments;
}
