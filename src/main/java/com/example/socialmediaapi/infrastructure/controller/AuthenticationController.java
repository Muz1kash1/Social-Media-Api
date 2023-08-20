package com.example.socialmediaapi.infrastructure.controller;

import com.example.socialmediaapi.infrastructure.service.authentication.TokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@AllArgsConstructor
public class AuthenticationController {
  private final TokenService tokenService;

  @PostMapping(value = "/signin")
  public String userLogin(Authentication authentication){
    log.info("token generated for '{}'",authentication.getName());
    String token = tokenService.generateToken(authentication);
    log.info("token granted {}", token);
    return token;
  }
}
