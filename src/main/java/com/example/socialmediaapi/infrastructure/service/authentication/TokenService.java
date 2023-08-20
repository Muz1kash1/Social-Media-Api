package com.example.socialmediaapi.infrastructure.service.authentication;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@Slf4j
@AllArgsConstructor
public class TokenService {
  private final JwtEncoder encoder;

  public String generateToken(Authentication authentication) {
    Instant now = Instant.now();
    log.info(authentication.toString());
    log.info(authentication.getPrincipal().toString());
    JwtClaimsSet jwtClaimsSet =
      JwtClaimsSet.builder()
        .issuer("self")
        .issuedAt((now))
        .expiresAt(now.plus(1, ChronoUnit.HOURS))
        .subject(authentication.getName())
        .build();
    log.info(jwtClaimsSet.getClaims().toString());
    return this.encoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
  }

}
