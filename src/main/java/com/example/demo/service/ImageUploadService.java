package com.example.demo.service;

import com.example.demo.endpoint.event.EventProducer;
import com.example.demo.endpoint.event.model.ImageUploaded;
import com.example.demo.file.bucket.BucketComponent;
import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class ImageUploadService {

  private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png");

  private final UserService userService;
  private final BucketComponent bucketComponent;
  private final EventProducer<ImageUploaded> eventProducer;

  @SneakyThrows
  public void handleUpload(MultipartFile file, String email) {
    userService.getByEmail(email);

    String originalFileName = file.getOriginalFilename();
    String extension = extractExtension(originalFileName);
    if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
      throw new IllegalArgumentException(
          "Format non supporte : ." + extension + " (seuls .jpg et .png sont acceptes)");
    }

    String bucketKey = UUID.randomUUID() + "-original." + extension;
    File tempFile = File.createTempFile("upload-", "." + extension);
    file.transferTo(tempFile);

    bucketComponent.upload(tempFile, bucketKey);

    var event =
        ImageUploaded.builder()
            .originalBucketKey(bucketKey)
            .fileName(originalFileName)
            .email(email)
            .build();
    eventProducer.accept(List.of(event));
  }

  private String extractExtension(String fileName) {
    if (fileName == null || !fileName.contains(".")) {
      throw new IllegalArgumentException(
          "Le fichier envoye doit avoir une extension (.jpg ou .png)");
    }
    return fileName.substring(fileName.lastIndexOf('.') + 1);
  }
}
