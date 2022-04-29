package com.example.blog.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "global_settings")
public class GlobalSettings {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private String code;
  private String name;
  private String value;
}
