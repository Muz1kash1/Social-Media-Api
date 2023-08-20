package com.example.socialmediaapi.infrastructure.service.authentication;

import com.example.socialmediaapi.infrastructure.repositories.jparepositories.postgres.UserRepository;
import com.example.socialmediaapi.infrastructure.service.authentication.userdetails.SecurityUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
@Slf4j
public class JpaUserDetailsService implements UserDetailsService {
  private final UserRepository userRepository;


  @Override public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
    return userRepository.findUserByUsername(username)
      .map(SecurityUser::new)
      .orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));
  }
}
