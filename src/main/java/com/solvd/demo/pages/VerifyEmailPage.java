package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class VerifyEmailPage extends AbstractPage {

    @FindBy(xpath = "//input[@type='submit' and @value='Send me an email'] | //button[normalize-space()='Send me an email']")
    private ExtendedWebElement sendMeAnEmailButton;

    @FindBy(xpath = "//h2[normalize-space()='Verify with your email']")
    private ExtendedWebElement verifyWithYourEmailHeading;

    @FindBy(xpath = "//a[normalize-space()='Enter a verification code instead'] | //button[normalize-space()='Enter a verification code instead']")
    private ExtendedWebElement enterVerificationCodeInsteadLink;

    @FindBy(xpath = "//a[normalize-space()='Back to Log In'] | //button[normalize-space()='Back to Log In']")
    private ExtendedWebElement backToLogInLink;

    public VerifyEmailPage(WebDriver driver) {
        super(driver);
    }

    public boolean isSendMeAnEmailButtonPresent() {
        return sendMeAnEmailButton.isPresent(20);
    }

    public void clickSendMeAnEmail() {
        sendMeAnEmailButton.click();
    }

    public boolean isVerifyWithYourEmailHeadingPresent() {
        return verifyWithYourEmailHeading.isPresent(30);
    }

    public String getVerifyWithYourEmailHeadingText() {
        return verifyWithYourEmailHeading.getText();
    }

    public boolean isEnterVerificationCodeInsteadLinkPresent() {
        return enterVerificationCodeInsteadLink.isPresent(15);
    }

    public boolean isBackToLogInLinkPresent() {
        return backToLogInLink.isPresent(15);
    }
}
