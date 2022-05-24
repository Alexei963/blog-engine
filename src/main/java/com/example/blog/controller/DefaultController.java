package com.example.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Api Default Controller.
 */

@Controller
public class DefaultController {

  @GetMapping("/")
  public String index() {
    return "index";
  }
}
