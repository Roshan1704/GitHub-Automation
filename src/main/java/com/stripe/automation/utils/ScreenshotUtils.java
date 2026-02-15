package com.stripe.automation.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ScreenshotUtils {
    private ScreenshotUtils() {}

    public static Path capture(WebDriver driver, String fileName) {
        byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        Path path = Path.of("target", "screenshots", fileName + ".png");
        try {
            Files.createDirectories(path.getParent());
            Files.write(path, screenshot);
            return path;
        } catch (IOException e) {
            throw new RuntimeException("Unable to store screenshot", e);
        }
    }
}
