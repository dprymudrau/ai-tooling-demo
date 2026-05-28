package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class VerifyEmailConfirmationPage extends AbstractPage {

    @FindBy(xpath = "//h2[contains(normalize-space(),'Verify with your email')]")
    private ExtendedWebElement verifyWithYourEmailHeading;

    @FindBy(xpath = "//p[contains(normalize-space(),'We sent you a verification email')]")
    private ExtendedWebElement verificationEmailConfirmationMessage;

    public VerifyEmailConfirmationPage(WebDriver driver) {
        super(driver);
        waitForConfirmation();
    }

    private void waitForConfirmation() {
        try {
            new WebDriverWait(getDriver(), Duration.ofSeconds(60))
                    .until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//h2[contains(normalize-space(),'Verify with your email')]")));
        } catch (Exception ignored) {
            // assertions in the test will surface this
        }
    }

    public String getVerifyWithYourEmailHeadingText() {
        return verifyWithYourEmailHeading.getText();
    }

    public String getVerificationEmailConfirmationMessageText() {
        return verificationEmailConfirmationMessage.getText();
    }

    public boolean isVerifyWithYourEmailHeadingPresent() {
        return verifyWithYourEmailHeading.isPresent();
    }

    public boolean isVerificationEmailConfirmationMessagePresent() {
        return verificationEmailConfirmationMessage.isPresent();
    }
}
