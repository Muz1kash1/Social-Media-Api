package com.example.socialmediaapi.infrastructure.controller;

import com.example.socialmediaapi.infrastructure.service.PostService;
import com.example.socialmediaapi.model.dto.PostCreationDto;
import com.example.socialmediaapi.model.dto.PostDto;
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
public class PostController {
  private final PostService postService;

  @PostMapping("/posts")
  ResponseEntity<PostDto> createPost(@RequestBody PostCreationDto postCreationDto, JwtAuthenticationToken principal) {
    PostDto postDto = postService.createPost(postCreationDto, principal.getName());
    return ResponseEntity.created(URI.create("/posts/" + postDto.getId())).body(postDto);
  }

  @PutMapping("/posts/{postId}")
  ResponseEntity<PostDto> redactPost(@PathVariable long postId, @RequestBody PostCreationDto postCreationDto,
                                     JwtAuthenticationToken principal) throws AccessDeniedException {
    PostDto postDto = postService.redactPost(postId, postCreationDto, principal.getName());
    return ResponseEntity.ok().body(postDto);
  }

  @DeleteMapping("/posts/{postId}")
  ResponseEntity<Void> deletePost(@PathVariable long postId, JwtAuthenticationToken principal)
    throws AccessDeniedException {
    postService.deletePost(postId, principal.getName());
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/feed")
  ResponseEntity<List<PostDto>> getFeed(JwtAuthenticationToken principal,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size) {
    List<PostDto> postDtos = postService.getFeed(principal.getName(), page, size);
    return ResponseEntity.ok().body(postDtos);
  }

  @GetMapping("/users/{userId}/posts")
  ResponseEntity<List<PostDto>> getUserPosts(@PathVariable long userId,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size) {
    List<PostDto> postDtos = postService.getUserPosts(userId, page, size);
    return ResponseEntity.ok().body(postDtos);
  }
}
