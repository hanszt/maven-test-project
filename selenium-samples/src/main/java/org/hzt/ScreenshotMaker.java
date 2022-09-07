package org.hzt;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ScreenshotMaker {

    private ScreenshotMaker() {
    }

    public static Path takeScreenshotAndWriteToTargetFolder(TakesScreenshot takesScreenshot) {
        try {
            final var screenshotBytes = takesScreenshot.getScreenshotAs(OutputType.BYTES);
            final var screenShotsDirPath = Path.of("./target/screenshots");
            Files.createDirectories(screenShotsDirPath);
            final var timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_hh.mm.ss"));
            final var screenShotFilePath = Path.of(screenShotsDirPath + "/screenshot_" + timeStamp + ".png");
            Files.createFile(screenShotFilePath);
            return Files.write(screenShotFilePath, screenshotBytes);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
