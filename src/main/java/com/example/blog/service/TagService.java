package com.example.blog.service;

import com.example.blog.api.response.TagResponse;
import com.example.blog.dto.TagDto;
import com.example.blog.model.Tag;
import com.example.blog.repository.TagRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * Tag Service.
 */

@Service
public class TagService {

  private final TagRepository tagRepository;
  private final MapperService mapperService;

  public TagService(TagRepository tagRepository,
      MapperService mapperService) {
    this.tagRepository = tagRepository;
    this.mapperService = mapperService;
  }

  public TagResponse getTagResponse(String query) {
    TagResponse tagResponse = new TagResponse();
    ArrayList<Tag> tags = new ArrayList<>(tagRepository.findAll());
    if (query == null) {
      List<TagDto> tagDtoList = tags.stream()
            .map(mapperService::convertTagToDto)
            .collect(Collectors.toList());
      tagResponse.setTags(tagDtoList);
      return tagResponse;
    } else {
      Tag tag = tagRepository.findByName(query);
      tagResponse.setTags(Collections.singletonList(mapperService.convertTagToDto(tag)));
    }
    return tagResponse;
  }

  public double getTagWeight(Tag tag) {
    int totalAllPosts = tagRepository.findAll()// Общее количество постов.
        .stream()
        .mapToInt(t -> t.getPosts().size())
        .sum();
    double maxWeight = tagRepository.findAll()// Ненормированный вес самого популярного поста.
        .stream()
        .mapToDouble(tad -> (double) tad.getPosts().size() / totalAllPosts)
        .max()
        .orElseThrow(IllegalStateException::new);
    double coefficient = 1 / maxWeight; // Коэффициент для нормализации.
    int numberOfPostsByTag = tag.getPosts().size(); // Количество постов по тегу.
    double value = (double) numberOfPostsByTag / totalAllPosts; // Считаю ненормированный вес.
    return value * coefficient;
  }
}
