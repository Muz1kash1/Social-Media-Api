package com.example.socialmediaapi.infrastructure.controller;

import com.example.socialmediaapi.infrastructure.service.UserService;
import com.example.socialmediaapi.infrastructure.service.authentication.TokenService;
import com.example.socialmediaapi.model.dto.UserDto;
import com.example.socialmediaapi.model.dto.UserSignupDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.net.URI;

@RestController
@Slf4j
@AllArgsConstructor
@Tag(name = "Ендпоинты аутентификации")
public class AuthenticationController {
  private final TokenService tokenService;
  private final UserService userService;

  @SecurityRequirement(name = "Basic Auth")
  @Operation(summary = "Войти и получить токен", description = "Возвращает jwt токен при успешной авторизации")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Успешно авторизовались"),
    @ApiResponse(responseCode = "401", description = "Не авторизовались")
  })
  @GetMapping(value = "/signin")
  public ResponseEntity<String> userLogin(Authentication authentication) {
    String token = tokenService.generateToken(authentication);
    return ResponseEntity.ok().body(token);
  }

  @Operation(summary = "Зарегестрировать пользователя", description = "Регистрирует нового пользователя в системе")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Успешно зарегистрировались, пользователь создан"),
    @ApiResponse(responseCode = "500", description = "Ошибка создания пользователя")
  })
  @PostMapping(value = "/signup")
  public ResponseEntity<UserDto> userSignup(@RequestBody UserSignupDto userSignupDto) {
    UserDto userDto = userService.createUser(userSignupDto);
    return ResponseEntity.created(URI.create("/users/" + userDto.getId())).body(userDto);
  }
}
