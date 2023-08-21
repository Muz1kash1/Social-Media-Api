package com.example.socialmediaapi.infrastructure.service;

import com.example.socialmediaapi.model.dto.PostCreationDto;
import com.example.socialmediaapi.model.dto.PostDto;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PostService {
  public PostDto createPost(final PostCreationDto postCreationDto, final String name) {
    return null;
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
