package com.example.demo.service.event;

import com.example.demo.domain.entity.ImageMetadata;
import com.example.demo.repository.ImageMetadataRepository;
import com.example.demo.endpoint.event.model.ImageUploaded;
import com.example.demo.file.bucket.BucketComponent;
import com.example.demo.file.image.ImageBlackAndWhiteConverter;
import com.example.demo.mail.Email;
import com.example.demo.mail.Mailer;
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

        // Étape 4 : récupère l'image originale depuis S3.
        // NOTE: adapte cet appel à la signature exacte de
        // BucketComponent.download() présente dans ton fichier
        // file/bucket/BucketComponent.java (elle peut retourner un File,
        // ou attendre un File de destination en paramètre).
        File originalFile = File.createTempFile("original-", "." + extension);
        bucketComponent.download(event.getOriginalBucketKey(), originalFile);

        // Étape 4 (suite) : transforme l'image en noir et blanc.
        File transformedFile = ImageBlackAndWhiteConverter.toGrayscale(originalFile, extension);

        // Étape 5 : stocke l'image transformée sur S3.
        String transformedBucketKey = "bw-" + event.getOriginalBucketKey();
        bucketComponent.upload(transformedFile, transformedBucketKey);

        // Étape 6 : enregistre les métadonnées en base (Neon).
        ImageMetadata metadata =
                ImageMetadata.builder()
                        .fileName(event.getFileName())
                        .email(event.getEmail())
                        .createdAt(LocalDateTime.now())
                        .originalBucketKey(event.getOriginalBucketKey())
                        .transformedBucketKey(transformedBucketKey)
                        .build();
        imageMetadataRepository.save(metadata);

        // Étape 7 : envoie l'image noir et blanc par email.
        sendTransformedImageByEmail(event.getEmail(), transformedFile, event.getFileName());
    }

    private void sendTransformedImageByEmail(String to, File attachment, String originalFileName)
            throws Exception {
        InternetAddress recipient = new InternetAddress(to);
        // NOTE: adapte la construction de l'Email à la signature exacte de
        // com.example.demo.mail.Email (vérifie l'ordre des paramètres et le
        // type attendu pour les pièces jointes) présente dans ton projet.
        var email =
                new Email(
                        recipient,
                        List.of(),
                        List.of(),
                        "Votre image en noir et blanc",
                        "Bonjour,\n\nVeuillez trouver ci-joint la version noir et blanc de " + originalFileName + ".",
                        List.of(attachment));
        mailer.accept(email);
    }

    private String extractExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }
}