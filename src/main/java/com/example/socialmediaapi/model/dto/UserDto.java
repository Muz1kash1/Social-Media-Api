package com.example.socialmediaapi.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
  @Schema(name = "User id", example = "1", required = true)
  private final long id;
  @Schema(name = "User name", example = "username", required = true)
  private final String username;
  @Schema(name = "User mail", example = "username@mail.ru", required = true)
  private final String mail;
  @Schema(name = "User password", example = "123", required = true)
  private final String password;
}
