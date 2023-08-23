package com.example.socialmediaapi.infrastructure.mappers;

import com.example.socialmediaapi.model.domain.FriendRequest;
import com.example.socialmediaapi.model.dto.FriendRequestDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FriendRequestMapper {
  FriendRequestDto friendRequestToFriendRequestDto(FriendRequest friendRequest);

  FriendRequest friendRequestDtoToFriendRequest(FriendRequestDto friendRequestDto);

  FriendRequest friendRequestEntityToFriendRequest(
    com.example.socialmediaapi.infrastructure.repositories.entity.postgres.FriendRequest friendRequest);

  com.example.socialmediaapi.infrastructure.repositories.entity.postgres.FriendRequest friendRequestToFriendRequestEntity(
    FriendRequest friendRequest);
}
