package com.example.socialmediaapi.infrastructure.mappers;

import com.example.socialmediaapi.model.domain.Friendship;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FriendsMapper {
  Friendship friendshipEntityToFriendship(
    com.example.socialmediaapi.infrastructure.repositories.entity.postgres.Friendship friendship);

  com.example.socialmediaapi.infrastructure.repositories.entity.postgres.Friendship friendshipToFriendshipEntity(
    Friendship friendship);
}
