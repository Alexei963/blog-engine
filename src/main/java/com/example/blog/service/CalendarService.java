package com.example.blog.service;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;

import com.example.blog.api.response.CalendarResponse;
import com.example.blog.model.Post;
import com.example.blog.repository.PostRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * Calendar Service.
 */

@Service
public class CalendarService {

  private final PostRepository postRepository;

  public CalendarService(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  public CalendarResponse getCalendar(String year) {
    CalendarResponse calendarResponse = new CalendarResponse();
    List<Post> posts;
    Map<Object, Integer> map;
    if (year == null) {
      LocalDate currentYear = LocalDate.now();
      posts = postRepository.findPostsByDate(String.valueOf(currentYear.getYear()));
    } else {
      posts = postRepository.findPostsByDate(year);
    }
    map = posts.stream()
        .map(Post::getTime)
        .map(this::convertToLocalDate)
        .collect(groupingBy(s -> s, summingInt(s -> 1)));
    calendarResponse.setYears(postRepository.findYearsPosts());
    calendarResponse.setPosts(map);
    return calendarResponse;
  }

  private LocalDate convertToLocalDate(Date date) {
    return LocalDate.ofInstant(
        date.toInstant(), ZoneId.systemDefault());
  }
}
