package com.example.socialmediaapi.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Subscription {
  @NotNull
  private long id;
  @NotNull
  private long followerId;
  @NotNull
  private long followingId;
}
