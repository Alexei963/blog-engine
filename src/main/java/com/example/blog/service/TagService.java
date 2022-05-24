package com.example.blog.service;

import com.example.blog.api.response.TagResponse;
import com.example.blog.model.Tag;
import com.example.blog.repository.TagRepository;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * Tag Service.
 */

@Service
public class TagService {

  private final TagRepository tagRepository;

  public TagService(TagRepository tagRepository) {
    this.tagRepository = tagRepository;
  }

  public TagResponse getTagResponse(String query) {
    TagResponse tagResponse = new TagResponse();
    List<Tag> tags = tagRepository.findAll();
    ArrayList<Map<String, Object>> tagsList = new ArrayList<>();
    if (query == null) {
      for (Tag tag : tags) {
        tagsList.addAll(mappingInResponse(tag));
      }
      tagResponse.setTags(tagsList);
      return tagResponse;
    } else {
      Tag tag = tagRepository.findByName(query);
      tagResponse.setTags(mappingInResponse(tag));
    }
    return tagResponse;
  }

  private ArrayList<Map<String, Object>> mappingInResponse(Tag tag) {
    Map<String, Object> responseMap = new LinkedHashMap<>();
    ArrayList<Map<String, Object>> tagsList = new ArrayList<>();
    responseMap.put("name", tag.getName());
    responseMap.put("weight", getTagWeight(tag));
    tagsList.add(responseMap);
    return tagsList;
  }

  private double getTagWeight(Tag tag) {
    int totalAllPosts = tagRepository.findAll()// Общее количество постов.
        .stream()
        .mapToInt(t -> t.getPosts().size())
        .sum();
    double maxWeight = tagRepository.findAll()// Ненормированный вес самого популярного поста.
        .stream()
        .mapToDouble(tad -> (double) tad.getPosts().size() / totalAllPosts)
        .max()
        .getAsDouble();
    double coefficient = 1 / maxWeight; // Коэффициент для нормализации.
    int numberOfPostsByTag = tag.getPosts().size(); // Количество постов по тегу.
    double value = (double) numberOfPostsByTag / totalAllPosts; // Считаю ненормированный вес.
    return value * coefficient;
  }
}
