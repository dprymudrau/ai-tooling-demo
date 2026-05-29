package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class VerifyWithYourEmailPage extends AbstractPage {

    @FindBy(xpath = "//h2[normalize-space()='Verify with your email']")
    private ExtendedWebElement verificationConfirmationHeader;

    @FindBy(xpath = "//p[contains(normalize-space(.), 'We sent you a verification email')]")
    private ExtendedWebElement verificationConfirmationMessage;

    @FindBy(xpath = "//a[contains(normalize-space(.), 'Enter a verification code instead')] | //button[contains(normalize-space(.), 'Enter a verification code instead')]")
    private ExtendedWebElement enterVerificationCodeLink;

    @FindBy(xpath = "//a[normalize-space()='Back to Log In'] | //button[normalize-space()='Back to Log In']")
    private ExtendedWebElement backToLogInLink;

    public VerifyWithYourEmailPage(WebDriver driver) {
        super(driver);
    }

    public boolean isVerificationHeaderPresent() {
        return verificationConfirmationHeader.isElementPresent(30);
    }

    public String getVerificationHeaderText() {
        return verificationConfirmationHeader.getText();
    }

    public boolean isVerificationMessagePresent() {
        return verificationConfirmationMessage.isElementPresent(30);
    }

    public String getVerificationMessageText() {
        return verificationConfirmationMessage.getText();
    }

    public boolean isEnterVerificationCodeLinkPresent() {
        return enterVerificationCodeLink.isElementPresent(10);
    }

    public boolean isBackToLogInLinkPresent() {
        return backToLogInLink.isElementPresent(10);
    }
}
