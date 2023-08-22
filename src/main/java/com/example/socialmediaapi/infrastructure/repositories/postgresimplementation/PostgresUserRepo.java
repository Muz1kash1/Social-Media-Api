package com.example.socialmediaapi.infrastructure.repositories.postgresimplementation;

import com.example.socialmediaapi.infrastructure.mappers.UserMapper;
import com.example.socialmediaapi.infrastructure.repositories.IUserRepo;
import com.example.socialmediaapi.infrastructure.repositories.jparepositories.postgres.UserRepository;
import com.example.socialmediaapi.model.domain.User;
import com.example.socialmediaapi.model.dto.UserSignupDto;
import lombok.AllArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
@ComponentScan("com.example.socialmediaapi.infrastructure.repositories.entity.postgres")
public class PostgresUserRepo implements IUserRepo {
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Override public User createUser(final UserSignupDto userSignupDto) {
    User user =
      new User(
        1L,
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
}
