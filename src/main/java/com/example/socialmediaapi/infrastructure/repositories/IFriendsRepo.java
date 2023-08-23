package com.example.socialmediaapi.infrastructure.repositories;

import com.example.socialmediaapi.model.domain.ChatRequest;
import java.nio.file.AccessDeniedException;

public interface IFriendsRepo {
  void createFriendship(long receiverId, long senderId);

  void deleteFriend(long friendId, String name) throws AccessDeniedException;

  ChatRequest sendChatRequest(long receiverId, String name) throws AccessDeniedException;
}
