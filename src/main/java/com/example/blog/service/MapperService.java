package com.example.blog.service;

import com.example.blog.dto.CommentDto;
import com.example.blog.dto.OnePostDto;
import com.example.blog.dto.PostDto;
import com.example.blog.dto.TagDto;
import com.example.blog.dto.UserDto;
import com.example.blog.dto.UserDtoWithPhoto;
import com.example.blog.model.Post;
import com.example.blog.model.PostComment;
import com.example.blog.model.Tag;
import com.example.blog.model.User;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    postDto.setDislikeCount(post.getVotes().size());
    postDto.setCommentCount(post.getComments().size());
    return postDto;
  }

  public OnePostDto convertOnePostToDto(Post post) {
    OnePostDto onePostDto = mapper.map(post, OnePostDto.class);
    onePostDto.setTime(post.getTime().getTime() / 1000);
    boolean active = post.getIsActive() == 1;
    onePostDto.setActive(active);
    onePostDto.setUserDto(convertUserToDto(post.getUser()));
    onePostDto.setLikeCount(post.getVotes().size());
    onePostDto.setDislikeCount(post.getVotes().size());
    List<CommentDto> commentDtoList = new ArrayList<>();
    List<PostComment> postCommentList = post.getComments();
    postCommentList.forEach(postComment -> commentDtoList.add(convertCommentToDto(postComment)));
    onePostDto.setCommentsDto(commentDtoList);
    Set<String> stringSet = new HashSet<>();
    post.getTags().forEach(tag -> stringSet.add(tag.getName()));
    onePostDto.setTagsDto(stringSet);
    return onePostDto;
  }

  public TagDto convertTagToDto(Tag tag) {
    TagDto tagDto = mapper.map(tag, TagDto.class);
    tagDto.setWeight(tagService.getTagWeight(tag));
    return tagDto;
  }

  private UserDto convertUserToDto(User user) {
    return mapper.map(user, UserDto.class);
  }

  private UserDtoWithPhoto convertUserWithPhotoToDto(User user) {
    return mapper.map(user, UserDtoWithPhoto.class);
  }

  private CommentDto convertCommentToDto(PostComment postComment) {
    CommentDto commentDto = mapper.map(postComment, CommentDto.class);
    commentDto.setTime(postComment.getTime().getTime() / 1000);
    commentDto.setUserDtoWithPhoto(convertUserWithPhotoToDto(postComment.getUser()));
    return commentDto;
  }
}
