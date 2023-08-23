package com.example.socialmediaapi.infrastructure.repositories.postgresimplementation;

import com.example.socialmediaapi.infrastructure.mappers.ChatRequestMapper;
import com.example.socialmediaapi.infrastructure.mappers.FriendsMapper;
import com.example.socialmediaapi.infrastructure.repositories.entity.postgres.User;
import com.example.socialmediaapi.infrastructure.repositories.jparepositories.postgres.ChatRequestRepository;
import com.example.socialmediaapi.infrastructure.repositories.jparepositories.postgres.FriendsRepository;
import com.example.socialmediaapi.infrastructure.repositories.jparepositories.postgres.UserRepository;
import com.example.socialmediaapi.model.domain.ChatRequest;
import com.example.socialmediaapi.model.domain.Friendship;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тесты методов репозитория для работы с друзьями")
class PostgresFriendsRepoTest {
  @Mock
  private FriendsRepository friendsRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private ChatRequestRepository chatRequestRepository;

  @Mock
  private FriendsMapper friendsMapper;

  @Mock
  private ChatRequestMapper chatRequestMapper;

  @InjectMocks
  private PostgresFriendsRepo friendsRepo;

  @DisplayName("Тест метода для создания дружбы")
  @Test
  public void testCreateFriendship() {
    when(friendsMapper.friendshipToFriendshipEntity(any(Friendship.class)))
      .thenReturn(new com.example.socialmediaapi.infrastructure.repositories.entity.postgres.Friendship(1L, 1L, 2L));

    friendsRepo.createFriendship(1L, 2L);
    verify(friendsRepository, times(1)).save(
      any(com.example.socialmediaapi.infrastructure.repositories.entity.postgres.Friendship.class));
  }

  @DisplayName("Тест метода для удаления из друзей")
  @Test
  public void testDeleteFriend() {
    User user = new User(1L, "user1", "user1@example.com", "password");
    when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(user));

    com.example.socialmediaapi.infrastructure.repositories.entity.postgres.Friendship friendship =
      new com.example.socialmediaapi.infrastructure.repositories.entity.postgres.Friendship(1L, 1L, 2L);
    when(friendsRepository.getByUserIds(anyLong(), anyLong())).thenReturn(friendship);

    assertDoesNotThrow(() -> friendsRepo.deleteFriend(2L, "user1"));

    verify(friendsRepository, times(1)).deleteByUserIds(eq(1L), eq(2L));
  }

  @DisplayName("Тест метода для отправки запроса в друзья")
  @Test
  public void testSendChatRequest() {
    User user = new User(1L, "user1", "user1@example.com", "password");
    when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(user));

    com.example.socialmediaapi.infrastructure.repositories.entity.postgres.Friendship friendship =
      new com.example.socialmediaapi.infrastructure.repositories.entity.postgres.Friendship(1L, 1L, 2L);
    when(friendsRepository.getByUserIds(anyLong(), anyLong())).thenReturn(friendship);

    when(chatRequestMapper.chatRequestToChatRequestEntity(any(ChatRequest.class)))
      .thenReturn(new com.example.socialmediaapi.infrastructure.repositories.entity.postgres.ChatRequest());

    when(chatRequestRepository.findTopByOrderByIdDesc())
      .thenReturn(new com.example.socialmediaapi.infrastructure.repositories.entity.postgres.ChatRequest(1L, 1L, 2L, "pending"));

    assertDoesNotThrow(() -> friendsRepo.sendChatRequest(2L, "user1"));

    verify(chatRequestRepository, times(1)).save(any(com.example.socialmediaapi.infrastructure.repositories.entity.postgres.ChatRequest.class));
  }
}