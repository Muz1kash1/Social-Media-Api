package com.example.socialmediaapi.infrastructure.repositories;

import java.nio.file.AccessDeniedException;

public interface IFriendsRepo {
  void createFriendship(long receiverId, long senderId);

  void deleteFriend(long friendId, String name) throws AccessDeniedException;

  void sendChatRequest(long receiverId, String name) throws AccessDeniedException;
}
