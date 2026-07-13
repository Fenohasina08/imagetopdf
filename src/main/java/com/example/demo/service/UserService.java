package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public User createUser(String email) {
    if (userRepository.existsByEmail(email)) {
      throw new IllegalArgumentException("Un utilisateur avec cet email existe deja : " + email);
    }
    User user = User.builder().email(email).build();
    return userRepository.save(user);
  }

  public User getByEmail(String email) {
    return userRepository
        .findByEmail(email)
        .orElseThrow(
            () -> new EntityNotFoundException("Utilisateur introuvable pour l'email : " + email));
  }
}
