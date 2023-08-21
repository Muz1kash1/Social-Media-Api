package com.example.socialmediaapi.infrastructure.mappers;

import com.example.socialmediaapi.model.domain.User;
import com.example.socialmediaapi.model.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
  UserDto userToUserDto(User user);

  User userDtoToUser(UserDto userDto);

  User userEntityToUser(com.example.socialmediaapi.infrastructure.repositories.entity.postgres.User userEntity);

  com.example.socialmediaapi.infrastructure.repositories.entity.postgres.User userToUserEntity(User user);
}
