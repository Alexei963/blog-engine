package com.example.blog.service;

import com.example.blog.dto.PostDto;
import com.example.blog.dto.TagDto;
import com.example.blog.dto.UserDto;
import com.example.blog.model.Post;
import com.example.blog.model.Tag;
import com.example.blog.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * Mapper Service.
 */

@Service
public class MapperService {

  private final ModelMapper mapper;
  private final TagService tagService;

  public MapperService(ModelMapper mapper, @Lazy TagService tagService) {
    this.mapper = mapper;
    this.tagService = tagService;
  }

  public PostDto convertPostToDto(Post post) {
    PostDto postDto = mapper.map(post, PostDto.class);
    postDto.setTime(post.getTime().getTime() / 1000);
    postDto.setUserDto(convertUserToDto(post.getUser()));
    String announce = post.getText();
    if (announce.length() > 150) {
      announce = announce.substring(0, 150).concat("...");
    }
    postDto.setAnnounce(announce);
    postDto.setLikeCount(post.getVotes().size());
    postDto.setCommentCount(post.getComments().size());
    return postDto;
  }

  private UserDto convertUserToDto(User user) {
    return mapper.map(user, UserDto.class);
  }

  public TagDto convertTagToDto(Tag tag) {
    TagDto tagDto = mapper.map(tag, TagDto.class);
    tagDto.setWeight(tagService.getTagWeight(tag));
    return tagDto;
  }
}
