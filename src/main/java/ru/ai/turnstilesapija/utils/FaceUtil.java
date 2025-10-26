package ru.ai.turnstilesapija.utils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class FaceUtil {
    public static String encodeImage(String imagePath) throws Exception {
        byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));

        return Base64.getEncoder().encodeToString(imageBytes);
    }
}
