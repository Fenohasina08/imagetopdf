package com.example.demo.endpoint.rest.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ImageMetadataResponse {
  private UUID id;
  private String fileName;
  private String email;
  private LocalDateTime createdAt;
}
