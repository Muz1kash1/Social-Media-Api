package com.example.socialmediaapi.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
  private long id;
  @NotEmpty
  private String username;
  @NotEmpty
  private String mail;
  @NotEmpty
  private String password;
}
