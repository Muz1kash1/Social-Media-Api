package com.example.socialmediaapi.infrastructure.service;

import com.example.socialmediaapi.infrastructure.mappers.FriendRequestMapper;
import com.example.socialmediaapi.infrastructure.repositories.IFriendRequestsRepo;
import com.example.socialmediaapi.infrastructure.repositories.IFriendsRepo;
import com.example.socialmediaapi.infrastructure.repositories.ISubscriptionRepo;
import com.example.socialmediaapi.infrastructure.repositories.IUserRepo;
import com.example.socialmediaapi.model.domain.FriendRequest;
import com.example.socialmediaapi.model.dto.ChatRequestDto;
import com.example.socialmediaapi.model.dto.FriendRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.nio.file.AccessDeniedException;

@Service
@AllArgsConstructor
public class FriendsService {
  private final IFriendRequestsRepo friendRequestsRepo;
  private final ISubscriptionRepo subscriptionRepo;
  private final IUserRepo userRepo;
  private final FriendRequestMapper friendRequestMapper;
  private final IFriendsRepo friendsRepo;

  @Transactional
  public FriendRequestDto sendFriendRequest(final long receiverId, final String name) {
    subscriptionRepo.createSubscription(receiverId, name);
    FriendRequest request = friendRequestsRepo.createFriendRequest(receiverId, name);
    return friendRequestMapper.friendRequestToFriendRequestDto(request);
  }

  public FriendRequestDto rejectFriendRequest(final long requestId, final String name) throws AccessDeniedException {
    FriendRequest friendRequest = friendRequestsRepo.rejectFriendRequest(requestId, name);
    return friendRequestMapper.friendRequestToFriendRequestDto(friendRequest);
  }

  @Transactional
  public FriendRequestDto acceptFriendRequest(final long requestId, final String name) throws AccessDeniedException {
    FriendRequest friendRequest = friendRequestsRepo.acceptFriendRequest(requestId, name);
    subscriptionRepo.createSubscription(
      friendRequest.getSenderId(),
      userRepo.getUserById(friendRequest.getReceiverId()).getUsername()
    );

    friendsRepo.createFriendship(friendRequest.getReceiverId(), friendRequest.getSenderId());

    return friendRequestMapper.friendRequestToFriendRequestDto(friendRequest);
  }

  @Transactional
  public void deleteFriend(final long friendId, final String name) throws AccessDeniedException {
    subscriptionRepo.removeSubscription(name, friendId);
    friendsRepo.deleteFriend(friendId, name);
  }

  public void deleteFriendRequest(final long requestId, final String name) throws AccessDeniedException {
    friendRequestsRepo.deleteFriendRequest(requestId, name);
  }

  public ChatRequestDto sendChatRequest(final long receiverId, final String name) throws AccessDeniedException {
    friendsRepo.sendChatRequest(receiverId, name);
    return null;
  }
}
