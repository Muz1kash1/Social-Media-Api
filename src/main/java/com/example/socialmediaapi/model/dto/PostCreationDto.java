package com.example.socialmediaapi.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class PostCreationDto {
  @Schema(name = "title", example = "title", required = true)
  private final String title;
  @Schema(name = "text", example = "text", required = true)
  private final String text;
  @Schema(name = "creation time", example = "2024-08-19T10:00:00", required = true)
  private final LocalDateTime createdAt;
  @Schema(name = "List of pictures", example = "[\"пикча раз\",\"пикча 2\"]", required = true)
  private final List<String> pictures;

}
