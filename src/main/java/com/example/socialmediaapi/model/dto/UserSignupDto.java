package com.example.socialmediaapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserSignupDto {
  private final String username;
  private final String mail;
  private final String password;
}
