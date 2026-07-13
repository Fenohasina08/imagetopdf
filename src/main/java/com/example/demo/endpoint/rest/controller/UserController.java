package com.example.demo.endpoint.rest.controller;

import com.example.demo.endpoint.rest.dto.CreateUserRequest;
import com.example.demo.endpoint.rest.dto.UserResponse;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping
  public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
    User user = userService.createUser(request.getEmail());
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new UserResponse(user.getId(), user.getEmail()));
  }
}
