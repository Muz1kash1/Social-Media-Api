package com.example.socialmediaapi.infrastructure.service;

import com.example.socialmediaapi.infrastructure.mappers.PostMapper;
import com.example.socialmediaapi.infrastructure.repositories.IPostRepo;
import com.example.socialmediaapi.model.domain.Post;
import com.example.socialmediaapi.model.dto.PostCreationDto;
import com.example.socialmediaapi.model.dto.PostDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@DisplayName("Тесты на сервис связанный с постами")
@ExtendWith(MockitoExtension.class)
class PostServiceTest {

  @Mock
  private IPostRepo postRepo;
  @Mock
  private PostMapper postMapper;
  @InjectMocks
  private PostService postService;

  @DisplayName("тест на создание поста")
  @Test
  void createPost() {
    PostCreationDto postCreationDto = new PostCreationDto("title", "text", LocalDateTime.now(), List.of("picture"));
    Post expectedPost = new Post(1L, 1L, "title", "text", LocalDateTime.now(), List.of("picture"));
    PostDto expectedPostDto = new PostDto(1L, 1L, "title", "text", LocalDateTime.now(), List.of("picture"));

    when(postRepo.createPost(any(PostCreationDto.class), anyString())).thenReturn(expectedPost);
    when(postMapper.postToPostDto(any(Post.class))).thenReturn(expectedPostDto);

    PostDto result = postService.createPost(postCreationDto, "username");
    assertEquals(expectedPostDto, result);
    verify(postRepo, times(1)).createPost(eq(postCreationDto), eq("username"));
    verify(postMapper, times(1)).postToPostDto(eq(expectedPost));
  }

  @DisplayName("тест на успешное редактирование поста")
  @Test
  void testRedactPost_SuccessfulRedaction() throws AccessDeniedException {
    long postId = 1L;
    PostCreationDto postCreationDto = new PostCreationDto("title", "text", LocalDateTime.now(), List.of("picture"));
    String username = "username";
    Post updatedPost = new Post(1L, 1L, "newtitle", "newtext", LocalDateTime.now(), List.of("newpicture"));
    PostDto expectedPostDto = new PostDto(1L, 1L, "newtitle", "newtext", LocalDateTime.now(), List.of("newpicture"));
    when(postRepo.redactPost(eq(postId), any(PostCreationDto.class), eq(username))).thenReturn(updatedPost);
    when(postMapper.postToPostDto(eq(updatedPost))).thenReturn(expectedPostDto);

    PostDto result = postService.redactPost(postId, postCreationDto, username);

    assertEquals(expectedPostDto, result);
    verify(postRepo, times(1)).redactPost(eq(postId), eq(postCreationDto), eq(username));
    verify(postMapper, times(1)).postToPostDto(eq(updatedPost));
  }

  @DisplayName("тест на выброс исключения на ошибку доступа при редактировании")
  @Test
  void testRedactPost_AcessDeniedException() throws AccessDeniedException {
    long postId = 1L;
    PostCreationDto postCreationDto = new PostCreationDto("title", "text", LocalDateTime.now(), List.of("picture"));
    String username = "username";
    when(postRepo.redactPost(eq(postId), any(PostCreationDto.class), eq(username)))
      .thenThrow(new AccessDeniedException("Access denied"));

    assertThrows(AccessDeniedException.class, () -> postService.redactPost(postId, postCreationDto, username));
    verify(postRepo, times(1)).redactPost(eq(postId), eq(postCreationDto), eq(username));
    verifyNoInteractions(postMapper);
  }

  @DisplayName("тест на успешное удаление поста")
  @Test
  void deletePost_SuccessfulDeletion() throws AccessDeniedException {
    long postId = 1L;
    String username = "username";
    doNothing().when(postRepo).deletePost(eq(postId), eq(username));

    assertDoesNotThrow(() -> postService.deletePost(postId, username));
    verify(postRepo, times(1)).deletePost(eq(postId), eq(username));
    verifyNoMoreInteractions(postMapper);
  }

  @DisplayName("тест на выброс исключения на ошибку доступа при удалении")
  @Test
  public void deletePost_AccessDeniedException() throws AccessDeniedException {
    long postId = 1L;
    String username = "username";
    doThrow(new AccessDeniedException("Access denied")).when(postRepo).deletePost(eq(postId), eq(username));

    assertThrows(AccessDeniedException.class, () -> postService.deletePost(postId, username));
    verify(postRepo, times(1)).deletePost(eq(postId), eq(username));
    verifyNoMoreInteractions(postMapper);
  }

  @DisplayName("тест на получение ленты активности")
  @Test
  void testGetFeed() {
    String userName = "testUser";
    int page = 0;
    int size = 10;

    List<Post> mockPosts = new ArrayList<>();
    for (int i = 0; i < 15; i++) {
      Post mockPost = new Post();
      mockPosts.add(mockPost);
    }

    when(postRepo.getFeed(eq(userName), eq(page), eq(size))).thenReturn(mockPosts);

    List<PostDto> mockPostDtos = new ArrayList<>();
    for (Post mockPost : mockPosts) {
      PostDto mockPostDto = new PostDto(
        mockPost.getId(),
        mockPost.getUserId(),
        mockPost.getTitle(),
        mockPost.getText(),
        mockPost.getCreatedAt(),
        mockPost.getPictures()
      );
      mockPostDtos.add(mockPostDto);
    }
    when(postMapper.postToPostDto(any(Post.class))).thenReturn(
      mockPostDtos.get(0), mockPostDtos.subList(1, 10).toArray(new PostDto[0]));

    List<PostDto> result = postService.getFeed(userName, page, size);

    assertEquals(10, result.size());
    verify(postRepo, times(1)).getFeed(eq(userName), eq(page), eq(size));
    verify(postMapper, times(10)).postToPostDto(any(Post.class));
  }

  @DisplayName("тест на получение постов пользователя")
  @Test
  void testGetUserPosts() {

    long userId = 1L;
    int page = 0;
    int size = 10;

    List<Post> mockPosts = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      Post mockPost = new Post();
      // Настройка mockPost
      mockPosts.add(mockPost);
    }

    when(postRepo.getUserPosts(eq(userId), eq(page), eq(size))).thenReturn(mockPosts);

    List<PostDto> mockPostDtos = new ArrayList<>();
    for (Post mockPost : mockPosts) {
      PostDto mockPostDto = new PostDto(
        mockPost.getId(),
        mockPost.getUserId(),
        mockPost.getTitle(),
        mockPost.getText(),
        mockPost.getCreatedAt(),
        mockPost.getPictures()
      );
      mockPostDtos.add(mockPostDto);
    }
    when(postMapper.postToPostDto(any(Post.class))).thenReturn(
      mockPostDtos.get(0), mockPostDtos.subList(1, 10).toArray(new PostDto[0]));

    List<PostDto> result = postService.getUserPosts(userId, page, size);

    assertEquals(10, result.size());
    verify(postRepo, times(1)).getUserPosts(eq(userId), eq(page), eq(size));
    verify(postMapper, times(10)).postToPostDto(any(Post.class));
  }
}