package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class VerifyWithEmailPage extends AbstractPage {

    @FindBy(xpath = "//h2[normalize-space()='Verify with your email']")
    private ExtendedWebElement verifyWithEmailHeading;

    @FindBy(xpath = "//*[contains(text(),'We sent you a verification email')]")
    private ExtendedWebElement verificationConfirmationMessage;

    public VerifyWithEmailPage(WebDriver driver) {
        super(driver);
    }

    public boolean isVerifyWithEmailHeadingPresent() {
        return verifyWithEmailHeading.isPresent(20);
    }

    public String getVerifyWithEmailHeadingText() {
        return verifyWithEmailHeading.getText();
    }

    public boolean isVerificationConfirmationMessagePresent() {
        return verificationConfirmationMessage.isPresent(20);
    }

    public String getVerificationConfirmationMessageText() {
        return verificationConfirmationMessage.getText();
    }
}
