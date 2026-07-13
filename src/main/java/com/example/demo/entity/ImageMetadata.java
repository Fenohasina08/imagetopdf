package com.example.demo.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "image_metadata")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageMetadata {

  @Id @GeneratedValue private UUID id;

  @Column(name = "file_name", nullable = false)
  private String fileName;

  @Column(nullable = false)
  private String email;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "original_bucket_key", nullable = false)
  private String originalBucketKey;

  @Column(name = "transformed_bucket_key", nullable = false)
  private String transformedBucketKey;
}
