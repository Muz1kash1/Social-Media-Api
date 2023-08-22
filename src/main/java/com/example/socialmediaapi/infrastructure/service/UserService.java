package com.example.socialmediaapi.infrastructure.service;

import com.example.socialmediaapi.infrastructure.mappers.UserMapper;
import com.example.socialmediaapi.infrastructure.repositories.IUserRepo;
import com.example.socialmediaapi.model.domain.User;
import com.example.socialmediaapi.model.dto.UserDto;
import com.example.socialmediaapi.model.dto.UserSignupDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
  private final IUserRepo userRepo;
  private final UserMapper userMapper;

  public UserDto createUser(final UserSignupDto userSignupDto) {
    User user = userRepo.createUser(userSignupDto);
    return
      userMapper.userToUserDto(user);
  }
}