package com.example.blog.controller;

import com.example.blog.api.response.CalendarResponse;
import com.example.blog.api.response.InitResponse;
import com.example.blog.api.response.SettingsResponse;
import com.example.blog.api.response.TagResponse;
import com.example.blog.service.CalendarService;
import com.example.blog.service.SettingsService;
import com.example.blog.service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
  private final CalendarService calendarService;

  public ApiGeneralController(InitResponse initResponse,
      SettingsService settingsService, TagService tagService,
      CalendarService calendarService) {
    this.initResponse = initResponse;
    this.settingsService = settingsService;
    this.tagService = tagService;
    this.calendarService = calendarService;
  }

  @GetMapping("/init")
  private ResponseEntity<InitResponse> init() {
    return new ResponseEntity<>(initResponse, HttpStatus.OK);
  }

  @GetMapping("/settings")
  private ResponseEntity<SettingsResponse> settings() {
    return new ResponseEntity<>(settingsService.getGlobalSettings(), HttpStatus.OK);
  }

  @GetMapping("/tag")
  private ResponseEntity<TagResponse> getTag(@RequestParam(required = false) String query) {
    return new ResponseEntity<>(tagService.getTagResponse(query), HttpStatus.OK);
  }

  @GetMapping("/calendar")
  private ResponseEntity<CalendarResponse> getCalendar(@RequestParam(required = false) String year) {
    return new ResponseEntity<>(calendarService.getCalendar(year), HttpStatus.OK);
  }
}
