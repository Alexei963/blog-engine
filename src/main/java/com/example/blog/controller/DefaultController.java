package com.example.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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

  @GetMapping(value = "/**/{path:[^\\\\.]*}")
  public ModelAndView redirectWithUsingForwardPrefix(ModelMap model, @PathVariable String path) {
    model.addAttribute("attribute", path);
    return new ModelAndView("forward:/", model);
  }
}
