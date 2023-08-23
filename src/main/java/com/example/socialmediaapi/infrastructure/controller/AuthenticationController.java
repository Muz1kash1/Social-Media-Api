package com.example.socialmediaapi.infrastructure.controller;

import com.example.socialmediaapi.infrastructure.service.UserService;
import com.example.socialmediaapi.infrastructure.service.authentication.TokenService;
import com.example.socialmediaapi.model.dto.UserDto;
import com.example.socialmediaapi.model.dto.UserSignupDto;
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
public class AuthenticationController {
  private final TokenService tokenService;
  private final UserService userService;

  @GetMapping(value = "/signin")
  public ResponseEntity<String> userLogin(Authentication authentication) {
    String token = tokenService.generateToken(authentication);
    return ResponseEntity.ok().body(token);
  }

  @PostMapping(value = "/signup")
  public ResponseEntity<UserDto> userSignup(@RequestBody UserSignupDto userSignupDto) {
    UserDto userDto = userService.createUser(userSignupDto);
    return ResponseEntity.created(URI.create("/users/" + userDto.getId())).body(userDto);
  }
}
