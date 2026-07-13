package com.example.demo.service.event;

import com.example.demo.endpoint.event.model.ImageUploaded;
import com.example.demo.entity.ImageMetadata;
import com.example.demo.file.bucket.BucketComponent;
import com.example.demo.file.image.ImageBlackAndWhiteConverter;
import com.example.demo.mail.Email;
import com.example.demo.mail.Mailer;
import com.example.demo.repository.ImageMetadataRepository;
import jakarta.mail.internet.InternetAddress;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ImageUploadedService implements Consumer<ImageUploaded> {

  private final BucketComponent bucketComponent;
  private final ImageMetadataRepository imageMetadataRepository;
  private final Mailer mailer;

  @SneakyThrows
  @Override
  public void accept(ImageUploaded event) {
    String extension = extractExtension(event.getFileName());

    File originalFile = File.createTempFile("original-", "." + extension);
    bucketComponent.download(event.getOriginalBucketKey(), originalFile);

    File transformedFile = ImageBlackAndWhiteConverter.toGrayscale(originalFile, extension);

    String transformedBucketKey = "bw-" + event.getOriginalBucketKey();
    bucketComponent.upload(transformedFile, transformedBucketKey);

    ImageMetadata metadata =
        ImageMetadata.builder()
            .fileName(event.getFileName())
            .email(event.getEmail())
            .createdAt(LocalDateTime.now())
            .originalBucketKey(event.getOriginalBucketKey())
            .transformedBucketKey(transformedBucketKey)
            .build();
    imageMetadataRepository.save(metadata);

    sendTransformedImageByEmail(event.getEmail(), transformedFile, event.getFileName());
  }

  private void sendTransformedImageByEmail(String to, File attachment, String originalFileName)
      throws Exception {
    InternetAddress recipient = new InternetAddress(to);
    var email =
        new Email(
            recipient,
            List.of(),
            List.of(),
            "Votre image en noir et blanc",
            "Bonjour,\n\nVeuillez trouver ci-joint la version noir et blanc de "
                + originalFileName
                + ".",
            List.of(attachment));
    mailer.accept(email);
  }

  private String extractExtension(String fileName) {
    return fileName.substring(fileName.lastIndexOf('.') + 1);
  }
}
