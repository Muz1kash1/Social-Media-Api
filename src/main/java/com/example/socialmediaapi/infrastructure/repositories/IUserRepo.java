package com.example.socialmediaapi.infrastructure.repositories;

import com.example.socialmediaapi.model.domain.User;
import com.example.socialmediaapi.model.dto.UserSignupDto;

public interface IUserRepo {
  User createUser(UserSignupDto userSignupDto);
  User getUserById(long userId);
}
