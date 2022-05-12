package com.example.blog.controller;

import com.example.blog.api.response.InitResponse;
import com.example.blog.api.response.SettingsResponse;
import com.example.blog.api.response.TagResponse;
import com.example.blog.service.InitService;
import com.example.blog.service.SettingsService;
import com.example.blog.service.TagService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Api General Controller.
 */

@RestController
@RequestMapping("/api")
public class ApiGeneralController {

  private final InitService initService;
  private final SettingsService settingsService;
  private final TagService tagService;

  public ApiGeneralController(InitService initService,
      SettingsService settingsService, TagService tagService) {
    this.initService = initService;
    this.settingsService = settingsService;
    this.tagService = tagService;
  }

  @GetMapping("/init")
  private InitResponse init() {
    return initService.getInitResponse();
  }

  @GetMapping("/settings")
  private SettingsResponse settings() {
    return settingsService.getGlobalSettings();
  }

  @GetMapping("/tag")
  private TagResponse getTag() {
    return tagService.getTagResponse();
  }
}
