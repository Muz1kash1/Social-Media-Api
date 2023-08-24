package com.example.socialmediaapi.infrastructure.controller;

import com.example.socialmediaapi.infrastructure.service.FriendsService;
import com.example.socialmediaapi.model.dto.ChatRequestDto;
import com.example.socialmediaapi.model.dto.FriendRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
@DisplayName("Тесты на контроллер взаимодействий пользователей")
@WebMvcTest(FriendsController.class)
class FriendsControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private FriendsService friendsService;

  @DisplayName("Тест на посылку запроса в друзья")
  @Test
  void testSendFriendRequest() throws Exception {
    long receiverId = 2L;
    FriendRequestDto friendRequestDto = new FriendRequestDto(1L,1L,receiverId,"pending");
    when(friendsService.sendFriendRequest(eq(receiverId), anyString())).thenReturn(friendRequestDto);

    mockMvc.perform(post("/friend-requests")
        .with(jwt())
        .param("receiverId", String.valueOf(receiverId)))
      .andExpect(MockMvcResultMatchers.status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(friendRequestDto.getId()));
  }
  @DisplayName("Тест на принятие запроса в друзья")
  @Test
  void testAcceptFriendRequest() throws Exception {
    long requestId = 1L;
    FriendRequestDto friendRequestDto = new FriendRequestDto(requestId, 1L, 2L, "accepted");

    when(friendsService.acceptFriendRequest(eq(requestId), anyString())).thenReturn(friendRequestDto);

    mockMvc.perform(MockMvcRequestBuilders.put("/friend-requests/{requestId}/accept", requestId)
        .with(jwt()))
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(friendRequestDto.getId()));
  }

  @DisplayName("Тест на отклонение запроса в друзья")
  @Test
  void testRejectFriendRequest() throws Exception {
    long requestId = 1L;
    FriendRequestDto friendRequestDto = new FriendRequestDto(requestId, 1L, 2L, "rejected");

    when(friendsService.rejectFriendRequest(eq(requestId), anyString())).thenReturn(friendRequestDto);

    mockMvc.perform(MockMvcRequestBuilders.put("/friend-requests/{requestId}/reject", requestId)
        .with(jwt()))
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(friendRequestDto.getId()));
  }

  @DisplayName("Тест на удаление запроса в друзья")
  @Test
  void testDeleteFriendRequest() throws Exception {
    long requestId = 1L;

    mockMvc.perform(MockMvcRequestBuilders.delete("/friend-requests/{requestId}", requestId)
        .with(jwt()))
      .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  @DisplayName("Тест на удаление из друзей")
  @Test
  void testDeleteFriend() throws Exception {
    long friendId = 1L;

    mockMvc.perform(MockMvcRequestBuilders.delete("/friends/{friendId}", friendId)
        .with(jwt()))
      .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  @DisplayName("Тест на посылку запроса на переписку")
  @Test
  void testSendChatRequest() throws Exception {
    long receiverId = 2L;
    ChatRequestDto chatRequestDto = new ChatRequestDto(1L, 1L, receiverId, "pending");

    when(friendsService.sendChatRequest(eq(receiverId), anyString())).thenReturn(chatRequestDto);

    mockMvc.perform(post("/chat-requests")
        .with(jwt())
        .param("receiverId", String.valueOf(receiverId)))
      .andExpect(MockMvcResultMatchers.status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(chatRequestDto.getId()));
  }
}