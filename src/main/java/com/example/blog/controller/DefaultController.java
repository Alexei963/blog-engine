package com.example.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Api Default Controller.
 */

@Controller
@RequestMapping("/")
public class DefaultController {

  @GetMapping()
  public String index() {
    return "index";
  }

  @GetMapping(value = "/**/{path:[^\\.]*}")
  public String redirectToIndex() {
    return "forward:/";
  }
}
