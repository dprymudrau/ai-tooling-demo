package com.solvd.demo;

import com.zebrunner.carina.core.IAbstractTest;
import com.zebrunner.agent.core.registrar.Artifact;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;

import java.io.File;

public class BaseTest implements IAbstractTest {

    @AfterMethod(alwaysRun = true)
    public void attachExecutionVideo() {
        // 1. capture the session id BEFORE the driver is quit
        String sessionId;
        try {
            WebDriver driver = getDriver();
            sessionId = ((RemoteWebDriver) driver).getSessionId().toString();
        } catch (Exception e) {
            return; // no live driver (e.g. API test) — nothing to attach
        }

        // 2. end the session so ffmpeg flushes and finalizes the file
        quitDriver();

        // 3. wait for the finalized file (brief flush delay after quit)
        File video = new File(System.getProperty("user.dir"), "../videos/" + sessionId + ".mp4");
        for (int i = 0; i < 20 && (!video.exists() || video.length() == 0); i++) {
            try { Thread.sleep(500); } catch (InterruptedException ignored) { Thread.currentThread().interrupt(); }
        }

        // 4. upload to the current Zebrunner test
        if (video.exists() && video.length() > 0) {
            Artifact.attachToTest("execution-video.mp4", video);
        }
    }
}
