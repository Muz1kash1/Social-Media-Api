package com.example.socialmediaapi.infrastructure.mappers;

import com.example.socialmediaapi.model.domain.Post;
import com.example.socialmediaapi.model.dto.PostDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostMapper {
  PostDto postToPostDto(Post post);

  Post postDtoToPost(PostDto postDto);

  Post postEntityToPost(com.example.socialmediaapi.infrastructure.repositories.entity.postgres.Post postEntity);

  com.example.socialmediaapi.infrastructure.repositories.entity.postgres.Post postToPostEntity(Post post);
}
