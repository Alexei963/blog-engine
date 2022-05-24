package com.example.blog.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "captcha_codes")
public class Captcha {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private Date time;
  private String code;
  @Column(name = "secret_code")
  private String secretCode;
}
