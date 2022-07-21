package com.example.blog.service;

import com.example.blog.dto.CommentDto;
import com.example.blog.dto.PostDto;
import com.example.blog.dto.TagDto;
import com.example.blog.dto.UserDto;
import com.example.blog.model.Post;
import com.example.blog.model.PostComment;
import com.example.blog.model.Tag;
import com.example.blog.model.User;
import com.example.blog.repository.PostRepository;
import java.util.ArrayList;
import java.util.List;
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
  private final PostRepository postRepository;

  public MapperService(ModelMapper mapper, @Lazy TagService tagService,
      PostRepository postRepository) {
    this.mapper = mapper;
    this.tagService = tagService;
    this.postRepository = postRepository;
  }

  public PostDto convertPostToDto(Post post) {
    PostDto postDto = mapper.map(post, PostDto.class);
    postDto.setTime(post.getTime().getTime() / 1000);
    boolean active = post.getIsActive() == 1;
    postDto.setActive(active);
    postDto.setUserDto(convertUserToDto(post.getUser()));
    String announce = post.getText();
    if (announce.length() > 150) {
      announce = announce.substring(0, 150).concat("...");
    }
    postDto.setAnnounce(announce);
    long likesCount = post.getVotes().stream()
        .filter(postVote -> postVote.getValue() == 1)
        .count();
    postDto.setLikeCount((int) likesCount);
    long dislikesCount = post.getVotes().stream()
        .filter(postVote -> postVote.getValue() == -1)
        .count();
    postDto.setDislikeCount((int) dislikesCount);
    postDto.setCommentCount(post.getComments().size());
    List<CommentDto> commentDtoList = new ArrayList<>();
    List<PostComment> postCommentList = post.getComments();
    postCommentList.forEach(postComment -> commentDtoList.add(convertCommentToDto(postComment)));
    postDto.setCommentsDto(commentDtoList);
    List<String> tagsList = new ArrayList<>();
    post.getTags().forEach(tag -> tagsList.add(tag.getName()));
    postDto.setTagsDto(tagsList);
    return postDto;
  }

  public TagDto convertTagToDto(Tag tag) {
    TagDto tagDto = mapper.map(tag, TagDto.class);
    tagDto.setWeight(tagService.getTagWeight(tag));
    return tagDto;
  }

  public UserDto convertUserToDto(User user) {
    UserDto userDto = mapper.map(user, UserDto.class);
    boolean moderation = user.getIsModerator() == 1;
    userDto.setModeration(moderation);
    if (moderation) {
      userDto.setModerationCount(postRepository.countByModerationStatus());
      userDto.setSettings(true);
    }
    return userDto;
  }

  public CommentDto convertCommentToDto(PostComment postComment) {
    CommentDto commentDto = mapper.map(postComment, CommentDto.class);
    commentDto.setTime(postComment.getTime().getTime() / 1000);
    commentDto.setUserDto(convertUserToDto(postComment.getUser()));
    return commentDto;
  }
}
