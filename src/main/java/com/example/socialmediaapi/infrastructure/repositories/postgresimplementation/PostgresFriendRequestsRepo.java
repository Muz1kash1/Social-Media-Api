package com.example.socialmediaapi.infrastructure.repositories.postgresimplementation;

import com.example.socialmediaapi.infrastructure.mappers.FriendRequestMapper;
import com.example.socialmediaapi.infrastructure.repositories.IFriendRequestsRepo;
import com.example.socialmediaapi.infrastructure.repositories.entity.postgres.User;
import com.example.socialmediaapi.infrastructure.repositories.jparepositories.postgres.FriendRequestRepository;
import com.example.socialmediaapi.infrastructure.repositories.jparepositories.postgres.UserRepository;
import com.example.socialmediaapi.model.domain.FriendRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import java.nio.file.AccessDeniedException;
import java.util.NoSuchElementException;

@AllArgsConstructor
@Repository
public class PostgresFriendRequestsRepo implements IFriendRequestsRepo {
  private final FriendRequestRepository friendRequestRepository;
  private final UserRepository userRepository;
  private final FriendRequestMapper friendRequestMapper;

  @Override
  public FriendRequest createFriendRequest(final long receiverId, final String name) {
    User user = userRepository
      .findUserByUsername(name)
      .orElseThrow(() -> new UsernameNotFoundException("Пользователя с именем " + name + " не существует"));
    FriendRequest friendRequest = new FriendRequest(
      0L,
      user.getId(),
      receiverId,
      "pending"
    );

    friendRequestRepository.save(friendRequestMapper.friendRequestToFriendRequestEntity(friendRequest));

    com.example.socialmediaapi.infrastructure.repositories.entity.postgres.FriendRequest friendRequestToReturn
      = friendRequestRepository.findTopByOrderByIdDesc();

    return friendRequestMapper.friendRequestEntityToFriendRequest(friendRequestToReturn);
  }

  @Override public FriendRequest rejectFriendRequest(final long requestId, final String name)
    throws AccessDeniedException {
    User user = userRepository
      .findUserByUsername(name)
      .orElseThrow(() -> new UsernameNotFoundException("Пользователя с именем " + name + " не существует"));
    com.example.socialmediaapi.infrastructure.repositories.entity.postgres.FriendRequest friendRequest
      = friendRequestRepository
      .findById(requestId)
      .orElseThrow(() -> new NoSuchElementException("Запроса с id " + requestId + " не существует"));

    if (user.getId() == friendRequest.getReceiverId()) {
      if (friendRequest.getStatus().equals("pending")) {

        FriendRequest friendRequestToChange = friendRequestMapper.friendRequestEntityToFriendRequest(friendRequest);
        friendRequestToChange.setStatus("rejected");

        friendRequestRepository.save(friendRequestMapper.friendRequestToFriendRequestEntity(friendRequestToChange));

        return friendRequestToChange;
      } else {
        throw new ResponseStatusException(HttpStatus.CONFLICT, "Запрос " + requestId + " не находится в ожидании");
      }
    } else {
      throw new AccessDeniedException("Пользователь с именем " + name + " не является получателем запроса на дружбу");
    }
  }

  @Override public FriendRequest acceptFriendRequest(final long requestId, final String name)
    throws AccessDeniedException {
    User user = userRepository
      .findUserByUsername(name)
      .orElseThrow(() -> new UsernameNotFoundException("Пользователя с именем " + name + " не существует"));
    com.example.socialmediaapi.infrastructure.repositories.entity.postgres.FriendRequest friendRequest
      = friendRequestRepository.getReferenceById(requestId);

    if (user.getId() == friendRequest.getReceiverId()) {
      FriendRequest friendRequestToChange = friendRequestMapper.friendRequestEntityToFriendRequest(friendRequest);
      friendRequestToChange.setStatus("accepted");

      friendRequestRepository.save(friendRequestMapper.friendRequestToFriendRequestEntity(friendRequestToChange));

      return friendRequestToChange;
    } else {
      throw new AccessDeniedException("Пользователь с именем " + name + " не является получателем запроса на дружбу");
    }
  }

  @Override public void deleteFriendRequest(final long requestId, final String name) throws AccessDeniedException {
    com.example.socialmediaapi.infrastructure.repositories.entity.postgres.FriendRequest friendRequest
      = friendRequestRepository.findById(requestId).orElseThrow(
      () -> new NoSuchElementException("Запроса с id " + requestId + " не существует")
    );
    User user = userRepository
      .findUserByUsername(name)
      .orElseThrow(() -> new UsernameNotFoundException("Пользователя с именем " + name + " не существует"));
    if (user.getId() == friendRequest.getSenderId()) {
      if (friendRequest.getStatus().equals("pending")){
        friendRequestRepository.deleteById(requestId);
      }
      else throw new ResponseStatusException(HttpStatus.CONFLICT, "Запрос " + requestId + " не находится в ожидании");
    } else {
      throw new AccessDeniedException("Пользователь с именем " + name + " не является отправителем запроса на дружбу");
    }
  }
}
