package com.example.socialmediaapi.infrastructure.service;

import com.example.socialmediaapi.infrastructure.mappers.PostMapper;
import com.example.socialmediaapi.infrastructure.repositories.IPostRepo;
import com.example.socialmediaapi.model.domain.Post;
import com.example.socialmediaapi.model.dto.PostCreationDto;
import com.example.socialmediaapi.model.dto.PostDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.nio.file.AccessDeniedException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

  public PostDto redactPost(final long postId, final PostCreationDto postCreationDto, final String name)
    throws AccessDeniedException {
    Post post = postRepo.redactPost(postId, postCreationDto, name);
    return
      postMapper.postToPostDto(post);
  }

  public void deletePost(final long postId, final String name) throws AccessDeniedException {
    postRepo.deletePost(postId, name);
  }

  public List<PostDto> getFeed(final String name, int page, int size) {
    List<Post> posts = postRepo.getFeed(name, page, size);

    Pageable pageable = PageRequest.of(page, size);
    int startIndex = pageable.getPageNumber() * pageable.getPageSize();
    int endIndex = Math.min(startIndex + pageable.getPageSize(), posts.size());

    if (startIndex < endIndex) {
      posts = posts.subList(startIndex, endIndex);
    } else {
      posts = Collections.emptyList();
    }
    return posts.stream()
      .map(postMapper::postToPostDto)
      .collect(Collectors.toList());
  }

  public List<PostDto> getUserPosts(final long userId, int page, int size) {
    List<Post> posts = postRepo.getUserPosts(userId, page, size);

    return posts.stream()
      .map(postMapper::postToPostDto)
      .collect(Collectors.toList());
  }
}
