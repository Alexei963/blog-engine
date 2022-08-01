package com.example.blog.controller;

import com.example.blog.api.request.CommentRequest;
import com.example.blog.api.request.EditProfileRequest;
import com.example.blog.api.request.ModerationRequest;
import com.example.blog.api.request.SettingsRequest;
import com.example.blog.api.response.CalendarResponse;
import com.example.blog.api.response.InitResponse;
import com.example.blog.api.response.ResultAndErrorsResponse;
import com.example.blog.api.response.ResultResponse;
import com.example.blog.api.response.SettingsResponse;
import com.example.blog.api.response.StatisticsResponse;
import com.example.blog.api.response.TagResponse;
import com.example.blog.model.User;
import com.example.blog.service.CalendarService;
import com.example.blog.service.CommentService;
import com.example.blog.service.ImageService;
import com.example.blog.service.ModerationService;
import com.example.blog.service.ProfileEditingService;
import com.example.blog.service.SettingsService;
import com.example.blog.service.StatisticsService;
import com.example.blog.service.TagService;
import com.example.blog.service.UserService;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
  private final CommentService commentService;
  private final ModerationService moderationService;
  private final ProfileEditingService profileEditingService;
  private final StatisticsService statisticsService;
  private final UserService userService;

  private static final int IMAGE_WIDTH = 300;

  @Value("${upload.path}")
  private String uploadPath;

  @Value("${userPhotos.path}")
  private String userPhotoPath;

  public ApiGeneralController(InitResponse initResponse,
      SettingsService settingsService, TagService tagService,
      CalendarService calendarService, ImageService imageService,
      CommentService commentService, ModerationService moderationService,
      ProfileEditingService profileEditingService,
      StatisticsService statisticsService, UserService userService) {
    this.initResponse = initResponse;
    this.settingsService = settingsService;
    this.tagService = tagService;
    this.calendarService = calendarService;
    this.imageService = imageService;
    this.commentService = commentService;
    this.moderationService = moderationService;
    this.profileEditingService = profileEditingService;
    this.statisticsService = statisticsService;
    this.userService = userService;
  }

  @GetMapping("/init")
  public ResponseEntity<InitResponse> init() {
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
  public ResponseEntity<CalendarResponse> getCalendar(
      @RequestParam(required = false) String year) {
    return new ResponseEntity<>(calendarService.getCalendar(year), HttpStatus.OK);
  }

  @PostMapping("/image")
  @PreAuthorize("hasAuthority('user:write')")
  public ResponseEntity<Object> getImage(
      @RequestParam("image") MultipartFile image) throws IOException {
    if (imageService.imageUploadErrors(image).getErrors().isEmpty()) {
      return new ResponseEntity<>(imageService.getImagePath(image, uploadPath, IMAGE_WIDTH),
          HttpStatus.OK);
    } else {
      return new ResponseEntity<>(imageService.imageUploadErrors(image), HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping("/comment")
  @PreAuthorize("hasAuthority('user:write')")
  public ResponseEntity<Object> addComment(@RequestBody CommentRequest commentRequest) {
    if (commentService.commentAddingErrors(commentRequest).getErrors().isEmpty()) {
      return new ResponseEntity<>(commentService.addComment(commentRequest),
          HttpStatus.OK);
    } else {
      return new ResponseEntity<>(commentService.commentAddingErrors(commentRequest),
          HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping("/moderation")
  @PreAuthorize("hasAuthority('user:moderate')")
  public ResponseEntity<ResultResponse> postModeration(
      @RequestBody ModerationRequest moderationRequest) {
    return new ResponseEntity<>(moderationService.postModeration(moderationRequest),
        HttpStatus.OK);
  }

  @PostMapping(value = "/profile/my", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  @PreAuthorize("hasAuthority('user:write')")
  public ResponseEntity<ResultAndErrorsResponse> editProfileWithPhoto(
      @RequestParam(required = false) MultipartFile photo,
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String email,
      @RequestParam(required = false) String password) throws IOException {
    if (!profileEditingService.profileChangeErrors(name,
        email, password).getErrors().isEmpty()) {
      return new ResponseEntity<>(profileEditingService.profileChangeErrors(
          name, email, password), HttpStatus.BAD_REQUEST);
    }
    if (!profileEditingService.imageChangeErrors(photo).getErrors().isEmpty()) {
      return new ResponseEntity<>(profileEditingService.imageChangeErrors(photo),
          HttpStatus.BAD_REQUEST);
    } else {
      return new ResponseEntity<>(profileEditingService.profileAndPhotoEditing(
          photo, name, email, password, userPhotoPath), HttpStatus.OK);
    }
  }

  @PostMapping(value = "/profile/my", consumes = {MediaType.APPLICATION_JSON_VALUE})
  @PreAuthorize("hasAuthority('user:write')")
  public ResponseEntity<ResultAndErrorsResponse> editProfileWithoutPhoto(
      @RequestBody EditProfileRequest editProfileRequest) {
    if (editProfileRequest.getRemovePhoto() == 1) {
      return new ResponseEntity<>(profileEditingService.deletePhoto(editProfileRequest),
          HttpStatus.OK);
    }
    if (!profileEditingService.profileChangeErrors(editProfileRequest.getName(),
        editProfileRequest.getEmail(), editProfileRequest.getPassword()).getErrors().isEmpty()) {
      return new ResponseEntity<>(profileEditingService.profileChangeErrors(
          editProfileRequest.getName(), editProfileRequest.getEmail(),
          editProfileRequest.getPassword()), HttpStatus.BAD_REQUEST);
    } else {
      return new ResponseEntity<>(profileEditingService.editProfileWithoutPhoto(
          editProfileRequest.getName(), editProfileRequest.getEmail(),
          editProfileRequest.getPassword()), HttpStatus.OK);
    }
  }

  @GetMapping("/statistics/my")
  @PreAuthorize("hasAuthority('user:write')")
  public ResponseEntity<StatisticsResponse> myStatistics() {
    return new ResponseEntity<>(statisticsService.getMyStatistics(), HttpStatus.OK);
  }

  @GetMapping("/statistics/all")
  public ResponseEntity<StatisticsResponse> allStatistics() {
    User user = userService.getLoggedUser();
    if (user != null && user.getIsModerator() != 1) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    return new ResponseEntity<>(statisticsService.getAllStatistics(), HttpStatus.OK);
  }

  @PutMapping("/settings")
  @PreAuthorize("hasAuthority('user:moderate')")
  public ResponseEntity<String> saveSettings(@RequestBody SettingsRequest settingsRequest) {
    settingsService.saveSettings(settingsRequest);
    return new ResponseEntity<>("", HttpStatus.OK);
  }
}
