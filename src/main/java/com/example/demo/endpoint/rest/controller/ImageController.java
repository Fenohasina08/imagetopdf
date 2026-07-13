package com.example.demo.endpoint.rest.controller;

import com.example.demo.domain.entity.ImageMetadata;
import com.example.demo.domain.repository.ImageMetadataRepository;
import com.example.demo.endpoint.rest.dto.ImageMetadataResponse;
import com.example.demo.service.ImageUploadService;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/images")
@AllArgsConstructor
public class ImageController {

  private final ImageUploadService imageUploadService;
  private final ImageMetadataRepository imageMetadataRepository;

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Void> uploadImage(
      @RequestParam("file") MultipartFile file, @RequestParam("email") String email) {
    imageUploadService.handleUpload(file, email);
    // 202 Accepted : le traitement (transformation + email) se fait en arrière-plan.
    return ResponseEntity.accepted().build();
  }

  @GetMapping
  public List<ImageMetadataResponse> getAllImages() {
    return imageMetadataRepository.findAll().stream().map(this::toResponse).toList();
  }

  @GetMapping("/{id}")
  public ResponseEntity<ImageMetadataResponse> getImageById(@PathVariable UUID id) {
    return imageMetadataRepository
        .findById(id)
        .map(this::toResponse)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  private ImageMetadataResponse toResponse(ImageMetadata entity) {
    return new ImageMetadataResponse(
        entity.getId(), entity.getFileName(), entity.getEmail(), entity.getCreatedAt());
  }
}
