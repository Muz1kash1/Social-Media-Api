package com.example.socialmediaapi.infrastructure.repositories.postgresimplementation;

import com.example.socialmediaapi.infrastructure.mappers.PostMapper;
import com.example.socialmediaapi.infrastructure.repositories.IPostRepo;
import com.example.socialmediaapi.infrastructure.repositories.jparepositories.postgres.PostRepository;
import com.example.socialmediaapi.infrastructure.repositories.jparepositories.postgres.UserRepository;
import com.example.socialmediaapi.model.domain.Post;
import com.example.socialmediaapi.model.dto.PostCreationDto;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class PostgresPostRepo implements IPostRepo {
  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final PostMapper postMapper;

  @Override
  public Post createPost(final PostCreationDto postCreationDto, final String name) {

    Post post =
      new Post(
        1L,
        userRepository
          .findUserByUsername(name)
          .orElseThrow(() -> new UsernameNotFoundException("Пользователя с именем " + name + " не существует"))
          .getId(),
        postCreationDto.getTitle(),
        postCreationDto.getText(),
        postCreationDto.getCreatedAt(),
        postCreationDto.getPictures()

      );

    postRepository.save(postMapper.postToPostEntity(post));

    com.example.socialmediaapi.infrastructure.repositories.entity.postgres.Post postToReturn
      = postRepository.findTopByOrderByIdDesc();
    return postMapper.postEntityToPost(postToReturn);
  }
}
