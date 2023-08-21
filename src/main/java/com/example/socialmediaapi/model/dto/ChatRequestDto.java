package com.example.socialmediaapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatRequestDto {
  private final long id;
  private final long senderId;
  private final long receiverId;
  private final String status;
}
