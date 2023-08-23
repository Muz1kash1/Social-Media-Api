package com.example.socialmediaapi.infrastructure.service;

import com.example.socialmediaapi.infrastructure.mappers.ChatRequestMapper;
import com.example.socialmediaapi.infrastructure.mappers.FriendRequestMapper;
import com.example.socialmediaapi.infrastructure.repositories.IFriendRequestsRepo;
import com.example.socialmediaapi.infrastructure.repositories.IFriendsRepo;
import com.example.socialmediaapi.infrastructure.repositories.ISubscriptionRepo;
import com.example.socialmediaapi.infrastructure.repositories.IUserRepo;
import com.example.socialmediaapi.model.domain.ChatRequest;
import com.example.socialmediaapi.model.domain.FriendRequest;
import com.example.socialmediaapi.model.domain.Subscription;
import com.example.socialmediaapi.model.domain.User;
import com.example.socialmediaapi.model.dto.ChatRequestDto;
import com.example.socialmediaapi.model.dto.FriendRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.nio.file.AccessDeniedException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


@DisplayName("Тесты на взаимодействие пользователей")
@ExtendWith(MockitoExtension.class)
class FriendsServiceTest {

  @Mock
  private IFriendRequestsRepo friendRequestsRepo;

  @Mock
  private ISubscriptionRepo subscriptionRepo;

  @Mock
  private IUserRepo userRepo;

  @Mock
  private IFriendsRepo friendsRepo;

  @Mock
  private FriendRequestMapper friendRequestMapper;

  @Mock
  private ChatRequestMapper chatRequestMapper;

  @InjectMocks
  private FriendsService friendsService;

  @DisplayName("Тест на отправку запроса в друзья")
  @Test
  void sendFriendRequest() {
    long receiverId = 2L;
    String senderName = "sender";

    Subscription subscription = new Subscription(1L, 1L, 2L);
    FriendRequest friendRequest = new FriendRequest(1L, 1L, 2L, "pending");
    FriendRequestDto friendRequestDto = new FriendRequestDto(1L, 1L, 2L, "pending");

    when(subscriptionRepo.createSubscription(eq(receiverId), eq(senderName))).thenReturn(subscription);
    when(friendRequestsRepo.createFriendRequest(eq(receiverId), eq(senderName))).thenReturn(friendRequest);
    when(friendRequestMapper.friendRequestToFriendRequestDto(any(FriendRequest.class))).thenReturn(friendRequestDto);

    FriendRequestDto result = friendsService.sendFriendRequest(receiverId, senderName);

    assertNotNull(result);
    assertEquals(friendRequestDto, result);
    verify(subscriptionRepo, times(1)).createSubscription(
      eq(receiverId), eq(senderName));
    verify(friendRequestsRepo, times(1)).createFriendRequest(
      eq(receiverId), eq(senderName));
    verify(friendRequestMapper, times(1)).friendRequestToFriendRequestDto(
      eq(friendRequest));
  }

  @Test
  @DisplayName("Тест на отклонение запроса в друзья")
  public void testRejectFriendRequest() throws AccessDeniedException {
    long requestId = 3L;
    String userName = "user";

    FriendRequest friendRequest = new FriendRequest(3L, 1L, 2L, "pending");
    FriendRequestDto friendRequestDto = new FriendRequestDto(3L, 1L, 2L, "rejected");

    when(friendRequestsRepo.rejectFriendRequest(eq(requestId), eq(userName))).thenReturn(friendRequest);
    when(friendRequestMapper.friendRequestToFriendRequestDto(any(FriendRequest.class))).thenReturn(friendRequestDto);

    FriendRequestDto result = friendsService.rejectFriendRequest(requestId, userName);

    assertNotNull(result);
    assertEquals(friendRequestDto, result);
    verify(friendRequestsRepo, times(1)).rejectFriendRequest(eq(requestId), eq(userName));
    verify(friendRequestMapper, times(1)).friendRequestToFriendRequestDto(eq(friendRequest));
  }

  @Test
  @DisplayName("Тест на выброс исключения ошибки доступа при отклонении запроса в друзья")
  public void testRejectFriendRequestThrowsException() throws AccessDeniedException {
    long requestId = 3L;
    String userName = "user";

    when(friendRequestsRepo.rejectFriendRequest(eq(requestId), eq(userName)))
      .thenThrow(new AccessDeniedException("Access denied"));

    assertThrows(AccessDeniedException.class, () -> {
      friendsService.rejectFriendRequest(requestId, userName);
    });
  }

  @DisplayName("тест метода приема заявки в друзья")
  @Test
  public void testAcceptFriendRequest() throws AccessDeniedException {
    long requestId = 3L;
    String userName = "user";

    FriendRequest friendRequest = new FriendRequest();
    friendRequest.setId(requestId);
    friendRequest.setSenderId(1L);
    friendRequest.setReceiverId(2L);
    friendRequest.setStatus("pending");

    when(friendRequestsRepo.acceptFriendRequest(eq(requestId), eq(userName)))
      .thenReturn(friendRequest);

    when(userRepo.getUserById(eq(friendRequest.getReceiverId())))
      .thenReturn(new User(1L, "receiver", "receiver@example.com", "password"));

    friendsService.acceptFriendRequest(requestId, userName);

    verify(subscriptionRepo, times(1)).createSubscription(eq(friendRequest.getSenderId()), anyString());
    verify(friendsRepo, times(1)).createFriendship(eq(friendRequest.getReceiverId()), eq(friendRequest.getSenderId()));
    verify(friendRequestMapper, times(1)).friendRequestToFriendRequestDto(eq(friendRequest));
    verifyNoMoreInteractions(subscriptionRepo, friendsRepo, friendRequestMapper);
  }

  @DisplayName("тест на выброс исключения ошибки доступа при попытке принять запрос в друзья")
  @Test
  public void testAcceptFriendRequestWithAccessDeniedException() throws AccessDeniedException {
    long requestId = 3L;
    String userName = "user";

    when(friendRequestsRepo.acceptFriendRequest(eq(requestId), eq(userName)))
      .thenThrow(new AccessDeniedException("Access denied"));

    assertThrows(AccessDeniedException.class, () -> {
      friendsService.acceptFriendRequest(requestId, userName);
    });

    verifyNoMoreInteractions(subscriptionRepo, friendsRepo, friendRequestMapper);
  }
  @DisplayName("Тест на удаление друга")
  @Test
  public void testDeleteFriend() throws AccessDeniedException {
    long friendId = 5L;
    String userName = "user";

    friendsService.deleteFriend(friendId, userName);

    verify(subscriptionRepo, times(1)).removeSubscription(eq(userName), eq(friendId));
    verify(friendsRepo, times(1)).deleteFriend(eq(friendId), eq(userName));

    verifyNoMoreInteractions(subscriptionRepo, friendsRepo);
  }

  @DisplayName("тест на выброс исключения ошибки доступа при удалении друга")
  @Test
  public void testDeleteFriendThrowsException() throws AccessDeniedException {
    long friendId = 5L;
    String userName = "user";

    doThrow(new AccessDeniedException("Access denied"))
      .when(subscriptionRepo).removeSubscription(eq(userName), eq(friendId));

    assertThrows(AccessDeniedException.class, () -> friendsService.deleteFriend(friendId, userName));

    verify(subscriptionRepo, times(1)).removeSubscription(eq(userName), eq(friendId));
    verifyNoMoreInteractions(subscriptionRepo, friendsRepo);
  }
  @DisplayName("тест на удаление запроса в друзья")
  @Test
  public void testDeleteFriendRequest() throws AccessDeniedException {
    long requestId = 5L;
    String userName = "user";

    friendsService.deleteFriendRequest(requestId, userName);

    verify(friendRequestsRepo, times(1)).deleteFriendRequest(eq(requestId), eq(userName));
    verifyNoMoreInteractions(friendRequestsRepo);
  }
  @DisplayName("тест на выброс исключения ошибки доступа при удалении запроса в друзья")
  @Test
  public void testDeleteFriendRequestWithException() throws AccessDeniedException {
    long requestId = 5L;
    String userName = "user";

    doThrow(new AccessDeniedException("Access denied")).when(friendRequestsRepo).deleteFriendRequest(eq(requestId), eq(userName));

    assertThrows(AccessDeniedException.class, () -> friendsService.deleteFriendRequest(requestId, userName));

    verify(friendRequestsRepo, times(1)).deleteFriendRequest(eq(requestId), eq(userName));
    verifyNoMoreInteractions(friendRequestsRepo);
  }

  @DisplayName("тест на отправку запроса на переписку")
  @Test
  public void testSendChatRequest() throws AccessDeniedException {
    long receiverId = 2L;
    String userName = "user";

    ChatRequest chatRequest = new ChatRequest();
    chatRequest.setId(1L);
    chatRequest.setSenderId(1L);
    chatRequest.setReceiverId(receiverId);
    chatRequest.setStatus("pending");

    ChatRequestDto chatRequestDto = new ChatRequestDto(
      1L,
      1L,
      receiverId,
      "pending"
    );
    when(friendsRepo.sendChatRequest(eq(receiverId), eq(userName)))
      .thenReturn(chatRequest);

    when(chatRequestMapper.chatRequestToChatRequestDto(eq(chatRequest)))
      .thenReturn(chatRequestDto);

    ChatRequestDto result = friendsService.sendChatRequest(receiverId, userName);

    assertNotNull(result);
    assertEquals(chatRequestDto.getId(), result.getId());
    assertEquals(chatRequestDto.getSenderId(), result.getSenderId());
    assertEquals(chatRequestDto.getReceiverId(), result.getReceiverId());
    assertEquals(chatRequestDto.getStatus(), result.getStatus());

    verify(friendsRepo, times(1)).sendChatRequest(eq(receiverId), eq(userName));
    verify(chatRequestMapper, times(1)).chatRequestToChatRequestDto(eq(chatRequest));
    verifyNoMoreInteractions(friendsRepo, chatRequestMapper);
  }

  @DisplayName("тест на выброс исключения ошибки доступа при отправку запроса на переписку")
  @Test
  public void testSendChatRequestThrowsAccessDeniedException() throws AccessDeniedException {
    long receiverId = 2L;
    String userName = "user";

    when(friendsRepo.sendChatRequest(eq(receiverId), eq(userName)))
      .thenThrow(new AccessDeniedException("Access denied"));

    assertThrows(AccessDeniedException.class, () -> friendsService.sendChatRequest(receiverId, userName));

    verify(friendsRepo, times(1)).sendChatRequest(eq(receiverId), eq(userName));
    verifyNoMoreInteractions(friendsRepo, chatRequestMapper);
  }

}