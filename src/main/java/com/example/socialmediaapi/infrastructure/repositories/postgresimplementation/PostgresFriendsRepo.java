package com.example.socialmediaapi.infrastructure.repositories.postgresimplementation;

import com.example.socialmediaapi.infrastructure.mappers.ChatRequestMapper;
import com.example.socialmediaapi.infrastructure.mappers.FriendsMapper;
import com.example.socialmediaapi.infrastructure.repositories.IFriendsRepo;
import com.example.socialmediaapi.infrastructure.repositories.entity.postgres.User;
import com.example.socialmediaapi.infrastructure.repositories.jparepositories.postgres.ChatRequestRepository;
import com.example.socialmediaapi.infrastructure.repositories.jparepositories.postgres.FriendsRepository;
import com.example.socialmediaapi.infrastructure.repositories.jparepositories.postgres.UserRepository;
import com.example.socialmediaapi.model.domain.ChatRequest;
import com.example.socialmediaapi.model.domain.Friendship;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import java.nio.file.AccessDeniedException;

@Repository
@AllArgsConstructor
public class PostgresFriendsRepo implements IFriendsRepo {
  private final FriendsRepository friendsRepository;
  private final UserRepository userRepository;
  private final ChatRequestRepository chatRequestRepository;
  private final FriendsMapper friendsMapper;
  private final ChatRequestMapper chatRequestMapper;

  @Override
  public void createFriendship(final long receiverId, final long senderId) {
    Friendship friendship = new Friendship(0L, receiverId, senderId);
    friendsRepository.save(friendsMapper.friendshipToFriendshipEntity(friendship));
  }

  @Override
  public void deleteFriend(final long friendId, final String name) throws AccessDeniedException {
    User user = userRepository
      .findUserByUsername(name)
      .orElseThrow(() -> new UsernameNotFoundException("Пользователя с именем " + name + " не существует"));
    com.example.socialmediaapi.infrastructure.repositories.entity.postgres.Friendship friendship
      = friendsRepository.getByUserIds(
      user.getId(), friendId);
    if (user.getId() == friendship.getUser1Id() || user.getId() == friendship.getUser2Id()) {
      friendsRepository.deleteByUserIds(user.getId(), friendId);
    } else {
      throw new AccessDeniedException(
        "Пользователь с именем " + name + " не является другом пользователя с id " + friendId);
    }
  }

  @Override public ChatRequest sendChatRequest(final long receiverId, final String name) throws AccessDeniedException {
    User user = userRepository
      .findUserByUsername(name)
      .orElseThrow(() -> new UsernameNotFoundException("Пользователя с именем " + name + " не существует"));
    com.example.socialmediaapi.infrastructure.repositories.entity.postgres.Friendship friendship
      = friendsRepository.getByUserIds(
      user.getId(), receiverId);
    if ((user.getId() == friendship.getUser1Id() && receiverId == friendship.getUser2Id()) ||
      (user.getId() == friendship.getUser2Id() && receiverId == friendship.getUser1Id())) {
      ChatRequest chatRequest = new ChatRequest(0L, user.getId(), receiverId, "pending");
      chatRequestRepository.save(chatRequestMapper.chatRequestToChatRequestEntity(chatRequest));
    } else {
      throw new AccessDeniedException(
        "Пользователь с именем " + name + " не является другом пользователя с id " + receiverId);
    }
    com.example.socialmediaapi.infrastructure.repositories.entity.postgres.ChatRequest chatRequest
      = chatRequestRepository.findTopByOrderByIdDesc();
    return chatRequestMapper.chatRequestEntityToChatRequest(chatRequest);
  }
}
