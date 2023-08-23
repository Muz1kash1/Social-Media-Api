package com.example.socialmediaapi.infrastructure.repositories.postgresimplementation;

import com.example.socialmediaapi.infrastructure.mappers.UserMapper;
import com.example.socialmediaapi.infrastructure.repositories.jparepositories.postgres.UserRepository;
import com.example.socialmediaapi.model.dto.UserSignupDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тесты методов репозитория для работы с пользователями")
class PostgresUserRepoTest {

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserMapper userMapper;

  @InjectMocks
  private PostgresUserRepo userRepo;

  @DisplayName("Тест на создание пользователя")
  @Test
  public void testCreateUser() {
    UserSignupDto userSignupDto = new UserSignupDto("user1", "user1@example.com", "password");
    com.example.socialmediaapi.model.domain.User user = new com.example.socialmediaapi.model.domain.User(
      1L, "user1", "user1@example.com", "encodedPassword");

    when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
    when(userMapper.userToUserEntity(any(com.example.socialmediaapi.model.domain.User.class))).thenReturn(
      new com.example.socialmediaapi.infrastructure.repositories.entity.postgres.User());
    when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(
      new com.example.socialmediaapi.infrastructure.repositories.entity.postgres.User(1L, "user1", "user1@example.com",
        "encodedPassword"
      )));
    when(userMapper.userEntityToUser(
      any(com.example.socialmediaapi.infrastructure.repositories.entity.postgres.User.class))).thenReturn(user);

    com.example.socialmediaapi.model.domain.User result = userRepo.createUser(userSignupDto);

    assertEquals(user, result);
    verify(userRepository, times(1)).save(
      any(com.example.socialmediaapi.infrastructure.repositories.entity.postgres.User.class));
  }

  @DisplayName("Тест метода на получение пользователя")
  @Test
  public void testGetUserById() {
    com.example.socialmediaapi.infrastructure.repositories.entity.postgres.User userEntity =
      new com.example.socialmediaapi.infrastructure.repositories.entity.postgres.User(
        1L, "user1", "user1@example.com", "encodedPassword");
    when(userRepository.getReferenceById(anyLong())).thenReturn(userEntity);

    com.example.socialmediaapi.model.domain.User user = new com.example.socialmediaapi.model.domain.User(
      1L, "user1", "user1@example.com", "encodedPassword");
    when(userMapper.userEntityToUser(
      any(com.example.socialmediaapi.infrastructure.repositories.entity.postgres.User.class))).thenReturn(user);

    com.example.socialmediaapi.model.domain.User result = userRepo.getUserById(1L);

    assertEquals(user, result);
  }
}