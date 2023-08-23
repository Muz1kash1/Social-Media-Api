package com.example.socialmediaapi.infrastructure.service;

import com.example.socialmediaapi.infrastructure.mappers.UserMapper;
import com.example.socialmediaapi.infrastructure.repositories.IUserRepo;
import com.example.socialmediaapi.model.domain.User;
import com.example.socialmediaapi.model.dto.UserDto;
import com.example.socialmediaapi.model.dto.UserSignupDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@DisplayName("Тесты на сервис пользователя")
class UserServiceTest {

  private IUserRepo userRepo;
  private UserMapper userMapper;
  private UserService userService;

  @BeforeEach
  void setUp() {
    userRepo = mock(IUserRepo.class);
    userMapper = mock(UserMapper.class);
    userService = new UserService(userRepo, userMapper);
  }

  @DisplayName("тест на создание пользователя")
  @Test
  void createUser() {
    UserSignupDto userSignupDto = new UserSignupDto("username", "mail", "password");
    User createdUser = new User(1L, "username", "mail", "password");
    UserDto expectedUserDto = new UserDto(1L, "username", "mail", "password");

    when(userRepo.createUser(userSignupDto)).thenReturn(createdUser);
    when(userMapper.userToUserDto(createdUser)).thenReturn(expectedUserDto);

    UserDto result = userService.createUser(userSignupDto);

    assertEquals(expectedUserDto, result);
    verify(userRepo, times(1)).createUser(userSignupDto);
    verify(userMapper, times(1)).userToUserDto(createdUser);
  }
}