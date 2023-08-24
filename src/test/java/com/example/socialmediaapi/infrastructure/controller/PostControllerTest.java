package com.example.socialmediaapi.infrastructure.controller;

import com.example.socialmediaapi.infrastructure.service.PostService;
import com.example.socialmediaapi.model.dto.PostCreationDto;
import com.example.socialmediaapi.model.dto.PostDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

@WebMvcTest(PostController.class)
@DisplayName("Тест контроллера для взаимодействия с постами")
class PostControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private PostService postService;

  @DisplayName("Тест на создание поста")
  @Test
  void testCreatePost() throws Exception {
    PostCreationDto postCreationDto = new PostCreationDto("title", "text", LocalDateTime.now(), List.of("picture"));
    PostDto postDto = new PostDto(1L, 1L, "title", "text", LocalDateTime.now(), List.of("picture"));

    when(postService.createPost(eq(postCreationDto), anyString())).thenReturn(postDto);

    mockMvc.perform(MockMvcRequestBuilders.post("/posts")
        .with(jwt())
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(postCreationDto)))
      .andExpect(MockMvcResultMatchers.status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(postDto.getId()));
  }

  @DisplayName("Тест на редактирование поста")
  @Test
  void testRedactPost() throws Exception {
    long postId = 1L;
    PostCreationDto postCreationDto = new PostCreationDto("title", "text", LocalDateTime.now(), List.of("picture"));
    PostDto postDto = new PostDto(1L, 1L, "title", "text", LocalDateTime.now(), List.of("picture"));

    when(postService.redactPost(eq(postId), eq(postCreationDto), anyString())).thenReturn(postDto);

    mockMvc.perform(MockMvcRequestBuilders.put("/posts/{postId}", postId)
        .with(jwt())
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(postCreationDto)))
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(postDto.getId()));
  }

  @DisplayName("Тест на удаление поста")
  @Test
  void testDeletePost() throws Exception {
    long postId = 1L;
    mockMvc.perform(MockMvcRequestBuilders.delete("/posts/{postId}", postId)
        .with(jwt()))
      .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  private String asJsonString(Object obj) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper.writeValueAsString(obj);
  }

}
