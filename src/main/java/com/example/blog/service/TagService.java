package com.example.blog.service;

import com.example.blog.api.response.TagResponse;
import com.example.blog.model.Tag;
import com.example.blog.repository.TagRepository;
import java.util.ArrayList;
import java.util.Collections;
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

  public TagResponse getTagResponse() {
    TagResponse tagResponse = new TagResponse();
    Iterable<Tag>tagIterable = tagRepository.findAll();
    Map<String, Object> responseMap;
    ArrayList<Map<String, Object>> tagsList = new ArrayList<>();
    List<Double> doubleList = new ArrayList<>(); // Лист для значение веса тэгов.
    int numberOfPublications = 0; // Общее количество публикаций.
    double coefficient; // Коэффициент для нормализации.
    double maxWeight; // Максимальный вес.
    for (Tag tag : tagIterable) {
      int postCount = tag.getPosts().size(); // Количество постов по тэгу
      numberOfPublications += postCount;
    }
    for (Tag tag : tagIterable) {
      int postCount = tag.getPosts().size();
      double value = (double) postCount / numberOfPublications; // Считаю ненормированный вес.
      doubleList.add(value); // Добавляю чтобы найти максимальный вес.
    }
    maxWeight = Collections.max(doubleList);
    coefficient = 1 / maxWeight; // Считаю коэффициент для нормализации.
    for (Tag tag : tagIterable) {
      int postCount = tag.getPosts().size();
      double value = (double) postCount / numberOfPublications;
      double weight = value * coefficient; // Для каждого тэга считаю его нормированный вес.
      responseMap = new LinkedHashMap<>();
      responseMap.put("name", tag.getName());
      responseMap.put("weight", weight);
      tagsList.add(responseMap);
    }
    tagResponse.setTags(tagsList);
    return tagResponse;
  }
}
