package com.example.socialmediaapi.infrastructure.controller;

import com.example.socialmediaapi.infrastructure.service.FriendsService;
import com.example.socialmediaapi.model.dto.FriendRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тест ендпоинтов отвечающих за взаимодействие пользователей")
@WebMvcTest(FriendsControllerTest.class)
class FriendsControllerTest {
  @MockBean
  private FriendsService friendsService;
  @Autowired
  private MockMvc mockMvc;


  @Test
  public void testSendFriendRequest() throws Exception {
    long receiverId = 1L;
    FriendRequestDto friendRequestDto = new FriendRequestDto(1L, 2L, receiverId, "pending");
    when(friendsService.sendFriendRequest(receiverId, "абобус")).thenReturn(friendRequestDto);

    TestingAuthenticationToken authenticationToken = new TestingAuthenticationToken("абобус", "1");
    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

    mockMvc.perform(MockMvcRequestBuilders.post("/friend-requests")
        .param("receiverId", String.valueOf(receiverId)))
      .andExpect(MockMvcResultMatchers.status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(friendRequestDto.getId()));
  }
}