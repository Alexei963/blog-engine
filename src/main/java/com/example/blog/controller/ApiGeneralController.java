package com.example.blog.controller;

import com.example.blog.api.response.InitResponse;
import com.example.blog.api.response.SettingsResponse;
import com.example.blog.api.response.TagResponse;
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

  private final InitResponse initResponse;
  private final SettingsService settingsService;
  private final TagService tagService;

  public ApiGeneralController(InitResponse initResponse,
                              SettingsService settingsService, TagService tagService) {
    this.initResponse = initResponse;
    this.settingsService = settingsService;
    this.tagService = tagService;
  }

  @GetMapping("/init")
  private InitResponse init() {
    return initResponse;
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
