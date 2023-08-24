package com.example.socialmediaapi.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatRequestDto {
  @Schema(name = "Chat request id", example = "1", required = true)
  private final long id;
  @Schema(name = "sender id", example = "1", required = true)
  private final long senderId;
  @Schema(name = "receiver id", example = "2", required = true)
  private final long receiverId;
  @Schema(name = "status", example = "pending", required = true)
  private final String status;
}
