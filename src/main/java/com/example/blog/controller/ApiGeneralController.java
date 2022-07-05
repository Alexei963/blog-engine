package com.example.blog.controller;

import com.example.blog.api.response.CalendarResponse;
import com.example.blog.api.response.InitResponse;
import com.example.blog.api.response.SettingsResponse;
import com.example.blog.api.response.TagResponse;
import com.example.blog.service.CalendarService;
import com.example.blog.service.ImageService;
import com.example.blog.service.SettingsService;
import com.example.blog.service.TagService;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
  private final ImageService imageService;

  @Value("${upload.path}")
  private String uploadPath;

  public ApiGeneralController(InitResponse initResponse,
      SettingsService settingsService, TagService tagService,
      CalendarService calendarService, ImageService imageService) {
    this.initResponse = initResponse;
    this.settingsService = settingsService;
    this.tagService = tagService;
    this.calendarService = calendarService;
    this.imageService = imageService;
  }

  @GetMapping("/init")
  private ResponseEntity<InitResponse> init() {
    return new ResponseEntity<>(initResponse, HttpStatus.OK);
  }

  @GetMapping("/settings")
  public ResponseEntity<SettingsResponse> settings() {
    return new ResponseEntity<>(settingsService.getGlobalSettings(), HttpStatus.OK);
  }

  @GetMapping("/tag")
  public ResponseEntity<TagResponse> getTag(@RequestParam(required = false) String query) {
    return new ResponseEntity<>(tagService.getTagResponse(query), HttpStatus.OK);
  }

  @GetMapping("/calendar")
  private ResponseEntity<CalendarResponse> getCalendar(
      @RequestParam(required = false) String year) {
    return new ResponseEntity<>(calendarService.getCalendar(year), HttpStatus.OK);
  }

  @PostMapping("/image")
  @PreAuthorize("hasAuthority('user:write')")
  public ResponseEntity<Object> getImage(
      @RequestParam("image") MultipartFile image) throws IOException {
    if (imageService.imageUploadErrors(image).getErrors().isEmpty()) {
      return new ResponseEntity<>(imageService.getImagePath(image, uploadPath), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(imageService.imageUploadErrors(image), HttpStatus.BAD_REQUEST);
    }
  }
}
