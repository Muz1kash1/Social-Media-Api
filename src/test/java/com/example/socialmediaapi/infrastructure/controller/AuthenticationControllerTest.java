package com.example.socialmediaapi.infrastructure.controller;

import com.example.socialmediaapi.infrastructure.service.UserService;
import com.example.socialmediaapi.infrastructure.service.authentication.TokenService;
import com.example.socialmediaapi.model.dto.UserDto;
import com.example.socialmediaapi.model.dto.UserSignupDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тест ендпоинтов регистрации и входов")
class AuthenticationControllerTest {
  @Mock
  private TokenService tokenService;
  @Mock
  private UserService userService;
  @InjectMocks
  private AuthenticationController authenticationController;

  @Test
  @DisplayName("Тест входа и получения токена")
  public void testUserLogin() throws Exception {
    Authentication authentication = mock(Authentication.class);
    String token = "generated-token";
    when(tokenService.generateToken(authentication)).thenReturn(token);

    ResponseEntity<String> response = authenticationController.userLogin(authentication);

    assertEquals(token, response.getBody());
    assertEquals(200, response.getStatusCode().value());

    verify(tokenService, times(1)).generateToken(authentication);

    verify(tokenService, times(1)).generateToken(authentication);
  }

  @DisplayName("Тест регистрации")
  @Test
  public void testUserSignup() {
    UserSignupDto userSignupDto = new UserSignupDto("username", "mail", "password");
    UserDto userDto = new UserDto(1L, "username", "mail", "password");
    when(userService.createUser(userSignupDto)).thenReturn(userDto);

    ResponseEntity<UserDto> response = authenticationController.userSignup(userSignupDto);

    assertEquals(userDto, response.getBody());
    assertEquals(201, response.getStatusCode().value());
    assertEquals(URI.create("/users/" + userDto.getId()), response.getHeaders().getLocation());

    verify(userService, times(1)).createUser(userSignupDto);
  }
}