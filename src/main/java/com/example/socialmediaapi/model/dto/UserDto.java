package com.example.socialmediaapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
  private final long id;
  private final String username;
  private final String mail;
  private final String password;
}
