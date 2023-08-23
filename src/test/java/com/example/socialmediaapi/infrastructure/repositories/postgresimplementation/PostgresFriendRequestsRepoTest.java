package com.example.socialmediaapi.infrastructure.repositories.postgresimplementation;

import com.example.socialmediaapi.infrastructure.mappers.FriendRequestMapper;
import com.example.socialmediaapi.infrastructure.repositories.entity.postgres.User;
import com.example.socialmediaapi.infrastructure.repositories.jparepositories.postgres.FriendRequestRepository;
import com.example.socialmediaapi.infrastructure.repositories.jparepositories.postgres.UserRepository;
import com.example.socialmediaapi.model.domain.FriendRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.nio.file.AccessDeniedException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("тесты методов постресовской реализации репозитория друзей")
class PostgresFriendRequestsRepoTest {
  @Mock
  private FriendRequestRepository friendRequestRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private FriendRequestMapper friendRequestMapper;

  @InjectMocks
  private PostgresFriendRequestsRepo friendRequestsRepo;

  @DisplayName("Тест создания запроса на дружбу")
  @Test
  public void testCreateFriendRequest() {
    User user = new User(1L, "user1", "user1@example.com", "password");

    when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(user));

    com.example.socialmediaapi.infrastructure.repositories.entity.postgres.FriendRequest savedFriendRequest =
      new com.example.socialmediaapi.infrastructure.repositories.entity.postgres.FriendRequest(1L, 1L, 2L, "pending");
    when(friendRequestRepository.save(any())).thenReturn(savedFriendRequest);
    when(friendRequestRepository.findTopByOrderByIdDesc()).thenReturn(savedFriendRequest);

    FriendRequest friendRequest = new FriendRequest(1L, 1L, 2L, "pending");
    when(friendRequestMapper.friendRequestToFriendRequestEntity(any())).thenReturn(savedFriendRequest);
    when(friendRequestMapper.friendRequestEntityToFriendRequest(any())).thenReturn(friendRequest);

    FriendRequest result = friendRequestsRepo.createFriendRequest(2L, "user1");

    assertNotNull(result);
    assertEquals(1L, result.getSenderId());
    assertEquals(2L, result.getReceiverId());
    assertEquals("pending", result.getStatus());

    verify(userRepository, times(1)).findUserByUsername(eq("user1"));
    verify(friendRequestRepository, times(1)).save(any());
    verify(friendRequestRepository, times(1)).findTopByOrderByIdDesc();
    verify(friendRequestMapper, times(1)).friendRequestToFriendRequestEntity(any());
    verify(friendRequestMapper, times(1)).friendRequestEntityToFriendRequest(any());

  }
  @DisplayName("Тест отклонения запроса на дружбу")
  @Test
  public void testRejectFriendRequest() throws AccessDeniedException {
    User user = new User(1L, "user1", "user1@example.com", "password");
    com.example.socialmediaapi.infrastructure.repositories.entity.postgres.FriendRequest friendRequestEntity =
      new com.example.socialmediaapi.infrastructure.repositories.entity.postgres.FriendRequest(1L, 2L, 1L, "pending");

    when(userRepository.findUserByUsername(eq("user1"))).thenReturn(Optional.of(user));
    when(friendRequestRepository.findById(eq(1L))).thenReturn(Optional.of(friendRequestEntity));

    FriendRequest friendRequest = new FriendRequest(1L, 2L, 1L, "pending");
    when(friendRequestMapper.friendRequestEntityToFriendRequest(any())).thenReturn(friendRequest);

    FriendRequest result = friendRequestsRepo.rejectFriendRequest(1L, "user1");

    assertNotNull(result);
    assertEquals(2L, result.getSenderId());
    assertEquals(1L, result.getReceiverId());
    assertEquals("rejected", result.getStatus());

    verify(userRepository, times(1)).findUserByUsername(eq("user1"));
    verify(friendRequestRepository, times(1)).findById(eq(1L));
    verify(friendRequestMapper, times(1)).friendRequestEntityToFriendRequest(any());
    verify(friendRequestRepository, times(1)).save(any());
  }
  @DisplayName("Тест принятия запроса на дружбу")
  @Test
  public void testAcceptFriendRequest() throws AccessDeniedException {
    User user = new User(1L, "user1", "user1@example.com", "password");
    com.example.socialmediaapi.infrastructure.repositories.entity.postgres.FriendRequest friendRequestEntity =
      new com.example.socialmediaapi.infrastructure.repositories.entity.postgres.FriendRequest(1L, 2L, 1L, "pending");

    when(userRepository.findUserByUsername(eq("user1"))).thenReturn(Optional.of(user));
    when(friendRequestRepository.getReferenceById(eq(1L))).thenReturn(friendRequestEntity);

    FriendRequest friendRequest = new FriendRequest(1L, 2L, 1L, "pending");
    when(friendRequestMapper.friendRequestEntityToFriendRequest(any())).thenReturn(friendRequest);

    FriendRequest result = friendRequestsRepo.acceptFriendRequest(1L, "user1");

    assertNotNull(result);
    assertEquals(2L, result.getSenderId());
    assertEquals(1L, result.getReceiverId());
    assertEquals("accepted", result.getStatus());

    verify(userRepository, times(1)).findUserByUsername(eq("user1"));
    verify(friendRequestRepository, times(1)).getReferenceById(eq(1L));
    verify(friendRequestMapper, times(1)).friendRequestEntityToFriendRequest(any());
    verify(friendRequestRepository, times(1)).save(any());
  }

  @DisplayName("Тест на удаление запроса дружбы")
  @Test
  public void testDeleteFriendRequest() {
    // Мокирование user и friendRequest
    User user = new User(1L, "user1", "user1@example.com", "password");
    com.example.socialmediaapi.infrastructure.repositories.entity.postgres.FriendRequest friendRequestEntity =
      new com.example.socialmediaapi.infrastructure.repositories.entity.postgres.FriendRequest(1L, 1L, 2L, "pending");

    when(userRepository.findUserByUsername(eq("user1"))).thenReturn(Optional.of(user));
    when(friendRequestRepository.findById(eq(1L))).thenReturn(Optional.of(friendRequestEntity));

    // Вызов тестируемого метода
    assertDoesNotThrow(() -> friendRequestsRepo.deleteFriendRequest(1L, "user1"));

    // Проверка вызовов методов моков
    verify(userRepository, times(1)).findUserByUsername(eq("user1"));
    verify(friendRequestRepository, times(1)).findById(eq(1L));
    verify(friendRequestRepository, times(1)).deleteById(eq(1L));
  }


}