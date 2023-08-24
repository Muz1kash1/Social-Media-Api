package com.example.socialmediaapi.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubscriptionDto {
  @Schema(name = "Subscription id", example = "1", required = true)
  private final long id;
  @Schema(name = "Follower id", example = "1", required = true)
  private final long followerId;
  @Schema(name = "Following id", example = "2", required = true)
  private final long followingId;
}
