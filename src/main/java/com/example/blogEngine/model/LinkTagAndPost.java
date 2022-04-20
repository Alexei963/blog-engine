package com.example.blogEngine.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tag2post")
public class LinkTagAndPost {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private int postId;
  private int tagId;
}
