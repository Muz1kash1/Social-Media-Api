package com.example.socialmediaapi.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserSignupDto {
  @Schema(name = "User name", example = "username", required = true)
  private final String username;
  @Schema(name = "User mail", example = "user@mail.ru", required = true)
  private final String mail;
  @Schema(name = "User password", example = "123", required = true)
  private final String password;
}
