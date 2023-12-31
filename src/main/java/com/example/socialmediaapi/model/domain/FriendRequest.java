package com.example.socialmediaapi.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class FriendRequest {
  @NotNull
  private long id;
  @NotNull
  private long senderId;
  @NotNull
  private long receiverId;
  @NotEmpty
  private String status;
}
