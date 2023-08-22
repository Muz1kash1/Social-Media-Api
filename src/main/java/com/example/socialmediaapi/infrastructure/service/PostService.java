package com.example.socialmediaapi.infrastructure.service;

import com.example.socialmediaapi.infrastructure.mappers.PostMapper;
import com.example.socialmediaapi.infrastructure.repositories.IPostRepo;
import com.example.socialmediaapi.model.domain.Post;
import com.example.socialmediaapi.model.dto.PostCreationDto;
import com.example.socialmediaapi.model.dto.PostDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class PostService {
  private final IPostRepo postRepo;
  private final PostMapper postMapper;

  public PostDto createPost(final PostCreationDto postCreationDto, final String name) {
    Post post = postRepo.createPost(postCreationDto, name);
    return
      postMapper.postToPostDto(post);
  }

  public PostDto redactPost(final long postId, final PostCreationDto postCreationDto, final String name) {
    return null;
  }

  public void deletePost(final long postId, final String name) {
  }

  public List<PostDto> getFeed(final String name) {
    return null;
  }

  public List<PostDto> getUserPosts(final long userId) {
    return null;
  }
}
