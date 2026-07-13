package com.example.demo.repository;

import com.example.demo.entity.ImageMetadata;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageMetadataRepository extends JpaRepository<ImageMetadata, UUID> {}
