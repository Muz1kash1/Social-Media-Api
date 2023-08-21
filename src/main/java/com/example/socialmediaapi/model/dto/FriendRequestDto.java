package com.example.socialmediaapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class FriendRequestDto {
  private final long id;
  private final long senderId;
  private final long receiverId;
  private final String status;
}
