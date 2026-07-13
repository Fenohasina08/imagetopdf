package com.example.demo.endpoint.event.model;

import java.time.Duration;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public record ImageUploadedService() {
} ImageUploaded extends PojaEvent {

    // Clé S3 de l'image originale déjà uploadée par ImageUploadService.
    private String originalBucketKey;

    // Nom de fichier original (ex : vacances.png), conservé pour les métadonnées.
    private String fileName;

    // Email de l'utilisateur ayant envoyé l'image, pour retrouver son compte
    // et lui envoyer le résultat.
    private String email;

    @Override
    public Duration maxConsumerDuration() {
        return Duration.ofSeconds(60);
    }

    @Override
    public Duration maxConsumerBackoffBetweenRetries() {
        return Duration.ofSeconds(30);
    }
}