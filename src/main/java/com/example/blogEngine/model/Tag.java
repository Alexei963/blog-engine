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
@Table(name = "tags")
public class Tag {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private String name;

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(name = "tag2post", joinColumns = @JoinColumn(name = "tag_id"),
          inverseJoinColumns = @JoinColumn(name = "post_id"))
  private List<Post> posts;
}
