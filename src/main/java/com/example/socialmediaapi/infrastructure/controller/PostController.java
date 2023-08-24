package com.example.socialmediaapi.infrastructure.controller;

import com.example.socialmediaapi.infrastructure.service.PostService;
import com.example.socialmediaapi.model.dto.PostCreationDto;
import com.example.socialmediaapi.model.dto.PostDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@AllArgsConstructor
@Tag(name = "Ендпоинты действий с постами")
public class PostController {
  private final PostService postService;

  @Operation(summary = "Создать пост", description = "Создает пост")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Пост создан"),
    @ApiResponse(responseCode = "500", description = "Ошибка создания поста")
  })
  @SecurityRequirement(name = "JWT token")
  @PostMapping("/posts")
  ResponseEntity<PostDto> createPost(@RequestBody PostCreationDto postCreationDto, JwtAuthenticationToken principal) {
    PostDto postDto = postService.createPost(postCreationDto, principal.getName());
    return ResponseEntity.created(URI.create("/posts/" + postDto.getId())).body(postDto);
  }
  @Operation(summary = "Редактировать пост", description = "Редактирует пост")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Пост отредактирован"),
    @ApiResponse(responseCode = "403", description = "Доступ запрещён")
  })
  @SecurityRequirement(name = "JWT token")
  @PutMapping("/posts/{postId}")
  ResponseEntity<PostDto> redactPost(@PathVariable long postId, @RequestBody PostCreationDto postCreationDto,
                                     JwtAuthenticationToken principal) throws AccessDeniedException {
    PostDto postDto = postService.redactPost(postId, postCreationDto, principal.getName());
    return ResponseEntity.ok().body(postDto);
  }
  @Operation(summary = "Удалить пост", description = "Удаляет пост")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "204", description = "Пост удалён"),
    @ApiResponse(responseCode = "403", description = "Доступ запрещён")
  })
  @SecurityRequirement(name = "JWT token")
  @DeleteMapping("/posts/{postId}")
  ResponseEntity<Void> deletePost(@PathVariable long postId, JwtAuthenticationToken principal)
    throws AccessDeniedException {
    postService.deletePost(postId, principal.getName());
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Получить ленту активности", description = "Получает ленту активности")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Лента активности получена"),
    @ApiResponse(responseCode = "401", description = "Не авторизован")
  })
  @SecurityRequirement(name = "JWT token")
  @GetMapping("/feed")
  ResponseEntity<List<PostDto>> getFeed(JwtAuthenticationToken principal,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size) {
    List<PostDto> postDtos = postService.getFeed(principal.getName(), page, size);
    return ResponseEntity.ok().body(postDtos);
  }
  @Operation(summary = "Получить посты пользователя", description = "Получает посты пользоваетеля")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Посты пользователя получены"),
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
  })
  @SecurityRequirement(name = "JWT token")
  @GetMapping("/users/{userId}/posts")
  ResponseEntity<List<PostDto>> getUserPosts(@PathVariable long userId,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size) {
    List<PostDto> postDtos = postService.getUserPosts(userId, page, size);
    return ResponseEntity.ok().body(postDtos);
  }
}
