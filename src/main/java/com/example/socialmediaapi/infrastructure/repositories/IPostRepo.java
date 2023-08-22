package com.example.socialmediaapi.infrastructure.repositories;

import com.example.socialmediaapi.model.domain.Post;
import com.example.socialmediaapi.model.dto.PostCreationDto;
import com.example.socialmediaapi.model.dto.PostDto;

public interface IPostRepo {
  Post createPost(PostCreationDto postCreationDto, String name);
}
