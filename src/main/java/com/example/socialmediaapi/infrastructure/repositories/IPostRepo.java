package com.example.socialmediaapi.infrastructure.repositories;

import com.example.socialmediaapi.model.domain.Post;
import com.example.socialmediaapi.model.dto.PostCreationDto;
import java.nio.file.AccessDeniedException;
import java.util.List;

public interface IPostRepo {
  Post createPost(PostCreationDto postCreationDto, String name);

  Post redactPost(long postId, PostCreationDto postCreationDto, String name) throws AccessDeniedException;

  void deletePost(long postId, String name) throws AccessDeniedException;

  List<Post> getFeed(String name, int page, int size);

  List<Post> getUserPosts(long userId, int page, int size);
}
