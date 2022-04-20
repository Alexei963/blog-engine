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
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private int isModerator;
  private java.sql.Timestamp regTime;
  private String name;
  private String email;
  private String password;
  private String code;
  private String photo;
}
