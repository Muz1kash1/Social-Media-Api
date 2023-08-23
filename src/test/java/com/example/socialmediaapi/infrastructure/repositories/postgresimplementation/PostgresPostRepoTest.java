package com.example.socialmediaapi.infrastructure.repositories.postgresimplementation;

import com.example.socialmediaapi.infrastructure.mappers.PostMapper;
import com.example.socialmediaapi.infrastructure.repositories.entity.postgres.Subscription;
import com.example.socialmediaapi.infrastructure.repositories.entity.postgres.User;
import com.example.socialmediaapi.infrastructure.repositories.jparepositories.postgres.PostRepository;
import com.example.socialmediaapi.infrastructure.repositories.jparepositories.postgres.SubscriptionRepository;
import com.example.socialmediaapi.infrastructure.repositories.jparepositories.postgres.UserRepository;
import com.example.socialmediaapi.model.domain.Post;
import com.example.socialmediaapi.model.dto.PostCreationDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тесты методов репозитория для работы с постами")
class PostgresPostRepoTest {
  @Mock
  private PostRepository postRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private SubscriptionRepository subscriptionRepository;

  @Mock
  private PostMapper postMapper;

  @InjectMocks
  private PostgresPostRepo postRepo;

  @DisplayName("Тест метода создания поста")
  @Test
  public void testCreatePost() {
    when(userRepository.findUserByUsername(anyString()))
      .thenReturn(Optional.of(new User(1L, "user1", "user1@example.com", "password")));
    when(postMapper.postToPostEntity(any(Post.class)))
      .thenReturn(new com.example.socialmediaapi.infrastructure.repositories.entity.postgres.Post());

    when(postRepository.findTopByOrderByIdDesc())
      .thenReturn(
        new com.example.socialmediaapi.infrastructure.repositories.entity.postgres.Post(1L, 1L, "title", "text",
          LocalDateTime.now(), List.of("picture")
        ));

    postRepo.createPost(new PostCreationDto("title", "text", LocalDateTime.now(), List.of("picture")), "user1");

    verify(postRepository, times(1)).save(
      any(com.example.socialmediaapi.infrastructure.repositories.entity.postgres.Post.class));
  }

  @DisplayName("Тест метода редактирования поста")
  @Test
  public void testRedactPost() throws AccessDeniedException {
    User user = new User(1L, "user1", "user1@example.com", "password");
    when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(user));

    com.example.socialmediaapi.infrastructure.repositories.entity.postgres.Post post =
      new com.example.socialmediaapi.infrastructure.repositories.entity.postgres.Post(1L, 1L, "title", "text",
        LocalDateTime.now(), List.of("picture")
      );
    when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));

    when(postRepository.save(any(com.example.socialmediaapi.infrastructure.repositories.entity.postgres.Post.class)))
      .thenReturn(
        new com.example.socialmediaapi.infrastructure.repositories.entity.postgres.Post(1L, 1L, "title", "text",
          LocalDateTime.now(), List.of("picture")
        ));

    postRepo.redactPost(1L, new PostCreationDto("title", "text", LocalDateTime.now(), List.of("picture")), "user1");

    verify(postRepository, times(1)).save(
      any(com.example.socialmediaapi.infrastructure.repositories.entity.postgres.Post.class));
  }

  @DisplayName("Тест метода удаления поста")
  @Test
  public void testDeletePost() {
    User user = new User(1L, "user1", "user1@example.com", "password");
    when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(user));

    com.example.socialmediaapi.infrastructure.repositories.entity.postgres.Post post =
      new com.example.socialmediaapi.infrastructure.repositories.entity.postgres.Post(1L, 1L, "title", "text",
        LocalDateTime.now(), List.of("picture")
      );
    when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));

    assertDoesNotThrow(() -> postRepo.deletePost(1L, "user1"));

    verify(postRepository, times(1)).deleteById(eq(1L));
  }

  @DisplayName("Тест метода получения постов пользователя")
  @Test
  public void testGetUserPosts() {
    Pageable pageable = Pageable.unpaged();
    List<com.example.socialmediaapi.infrastructure.repositories.entity.postgres.Post> postList =
      Collections.singletonList(
        new com.example.socialmediaapi.infrastructure.repositories.entity.postgres.Post(1L, 1L, "title", "text",
          LocalDateTime.now(), List.of("picture")
        ));
    Page<com.example.socialmediaapi.infrastructure.repositories.entity.postgres.Post> postPage =
      new PageImpl<>(postList, pageable, postList.size());
    when(postRepository.findAllByUserIdOrderByCreatedAtDesc(anyLong(), any(Pageable.class)))
      .thenReturn(postPage);

    when(postMapper.postEntityToPost(
      any(com.example.socialmediaapi.infrastructure.repositories.entity.postgres.Post.class)))
      .thenReturn(new Post());

    List<Post> result = postRepo.getUserPosts(1L, 0, 10);

    assertEquals(1, result.size());
  }

  @DisplayName("Тест метода получения ленты активности пользователя")
  @Test
  public void testGetFeed() {

    User user = new User(1L, "user1", "user1@example.com", "password");
    when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(user));

    List<Subscription> subscriptions = Collections.singletonList(new Subscription(1L, 1L, 2L));
    when(subscriptionRepository.findAllByFollowerId(anyLong())).thenReturn(subscriptions);

    List<com.example.socialmediaapi.infrastructure.repositories.entity.postgres.Post> postList =
      Collections.singletonList(
        new com.example.socialmediaapi.infrastructure.repositories.entity.postgres.Post(1L, 1L, "title", "text",
          LocalDateTime.now(), List.of("picture")
        ));
    when(postRepository.findAllByUserIdOrderByCreatedAtDesc(anyLong(), any(Pageable.class)))
      .thenReturn(new PageImpl<>(postList));

    when(postMapper.postEntityToPost(
      any(com.example.socialmediaapi.infrastructure.repositories.entity.postgres.Post.class)))
      .thenReturn(new Post());

    List<Post> result = postRepo.getFeed("user1", 0, 10);


    assertEquals(1, result.size());
  }

}