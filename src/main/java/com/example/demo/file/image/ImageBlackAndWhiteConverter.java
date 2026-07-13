package com.example.demo.file.image;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public final class ImageBlackAndWhiteConverter {

  private ImageBlackAndWhiteConverter() {}

  public static File toGrayscale(File input, String extension) throws IOException {
    BufferedImage original = ImageIO.read(input);
    if (original == null) {
      throw new IOException("Impossible de lire l'image : " + input.getName());
    }

    BufferedImage grayscale =
        new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
    Graphics2D graphics = grayscale.createGraphics();
    graphics.drawImage(original, 0, 0, null);
    graphics.dispose();

    String formatName = "png".equalsIgnoreCase(extension) ? "png" : "jpg";
    File output = File.createTempFile("bw-", "." + extension);
    ImageIO.write(grayscale, formatName, output);
    return output;
  }
}
