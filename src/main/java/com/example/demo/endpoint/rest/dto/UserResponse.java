package com.example.demo.endpoint.rest.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {
  private UUID id;
  private String email;
}
