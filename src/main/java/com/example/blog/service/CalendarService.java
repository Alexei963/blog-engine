package com.example.blog.service;

import com.example.blog.api.response.CalendarResponse;
import com.example.blog.model.Post;
import com.example.blog.repository.PostRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
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
    Set<Integer> years = StreamSupport
        .stream(postRepository.findAll().spliterator(), false)
        .map(post -> convertToLocalDate(post.getTime()).getYear())
        .collect(Collectors.toSet());
    Map<Object, Integer> map = new LinkedHashMap<>();
    if (year == null) {
      LocalDate currentYear = LocalDate.now();
      posts = postRepository.findPostsByDate(String.valueOf(currentYear.getYear()));
      posts.forEach(post -> map.put(convertToLocalDate(post.getTime()),
          postRepository.countPostsByTime(convertToLocalDate(post.getTime()).toString())));
    } else {
      posts = postRepository.findPostsByDate(year);
      posts.forEach(post -> map.put(convertToLocalDate(post.getTime()),
          postRepository.countPostsByTime(convertToLocalDate(post.getTime()).toString())));
    }
    List<Integer> sortedList = new ArrayList<>(years);
    Collections.sort(sortedList);
    calendarResponse.setYears(sortedList);
    calendarResponse.setPosts(map);
    return calendarResponse;
  }

  private LocalDate convertToLocalDate(Date date) {
    return LocalDate.ofInstant(
        date.toInstant(), ZoneId.systemDefault());
  }
}
