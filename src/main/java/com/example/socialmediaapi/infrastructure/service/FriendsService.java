package com.example.socialmediaapi.infrastructure.service;

import com.example.socialmediaapi.model.dto.ChatRequestDto;
import com.example.socialmediaapi.model.dto.FriendRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FriendsService {
  public FriendRequestDto sendFriendRequest(final long receiverId, final String name) {
    return null;
  }

  public FriendRequestDto acceptFriendRequest(final long requestId, final String name) {
    return null;
  }

  public FriendRequestDto rejectFriendRequest(final long requestId, final String name) {
    return null;
  }

  public void deleteFriend(final long friendId, final String name) {
  }

  public void deleteFriendRequest(final long requestId, final String name) {
  }

  public ChatRequestDto sendChatRequest(final long receiverId, final String name) {
    return null;
  }
}
