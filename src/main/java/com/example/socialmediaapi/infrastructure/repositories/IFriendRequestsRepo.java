package com.example.socialmediaapi.infrastructure.repositories;

import com.example.socialmediaapi.model.domain.FriendRequest;
import java.nio.file.AccessDeniedException;

public interface IFriendRequestsRepo {
  FriendRequest createFriendRequest(long receiverId, String name);

  FriendRequest rejectFriendRequest(long requestId, String name) throws AccessDeniedException;

  FriendRequest acceptFriendRequest(long requestId, String name) throws AccessDeniedException;

  void deleteFriendRequest(long requestId, String name) throws AccessDeniedException;
}
