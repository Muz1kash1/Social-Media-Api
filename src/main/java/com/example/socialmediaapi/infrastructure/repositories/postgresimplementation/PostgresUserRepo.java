package com.example.socialmediaapi.infrastructure.repositories.postgresimplementation;

import com.example.socialmediaapi.infrastructure.mappers.UserMapper;
import com.example.socialmediaapi.infrastructure.repositories.IUserRepo;
import com.example.socialmediaapi.infrastructure.repositories.jparepositories.postgres.UserRepository;
import com.example.socialmediaapi.model.domain.User;
import com.example.socialmediaapi.model.dto.UserSignupDto;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class PostgresUserRepo implements IUserRepo {
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Override
  public User createUser(final UserSignupDto userSignupDto) {
    User user =
      new User(
        0L,
        userSignupDto.getUsername(),
        userSignupDto.getMail(),
        passwordEncoder.encode(userSignupDto.getPassword())
      );

    userRepository.save(userMapper.userToUserEntity(user));

    com.example.socialmediaapi.infrastructure.repositories.entity.postgres.User userToReturn =
      userRepository
        .findUserByUsername(user.getUsername())
        .orElseThrow(
          () -> new RuntimeException("Пользователя " + user.getUsername() + " нет в базе")
        );
    return
      userMapper.userEntityToUser(userToReturn);
  }
  @Override
  public User getUserById(final long userId) {
    com.example.socialmediaapi.infrastructure.repositories.entity.postgres.User user = userRepository.getReferenceById(
      userId);
    return userMapper.userEntityToUser(user);
  }
}
