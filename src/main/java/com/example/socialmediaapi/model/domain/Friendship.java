package com.example.socialmediaapi.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Friendship {
  @NotNull
  private long id;
  @NotNull
  private long user1Id;
  @NotNull
  private long user2Id;
}
