package com.example.blog.service;

import com.example.blog.api.response.InitResponse;
import com.example.blog.model.Post;
import com.example.blog.repository.PostRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class InitService {

  private final PostRepository postsRepository;

  public InitService(PostRepository postsRepository) {
    this.postsRepository = postsRepository;
  }

  public InitResponse getInitResponse() {
    InitResponse initResponse = new InitResponse();
    Optional<Post> optionalPost = postsRepository.findById(1);
    Post post = null;
    if (optionalPost.isPresent()) {
      post = optionalPost.get();
    }
    assert post != null;
    initResponse.setTitle(post.getTitle());
    initResponse.setSubtitle("Тестовый подзаголовок");
    initResponse.setPhone("+79273355789");
    initResponse.setEmail(post.getUser().getEmail());
    initResponse.setCopyright(post.getUser().getName());
    initResponse.setCopyrightFrom(post.getTime().toString());
    return initResponse;
  }

}
