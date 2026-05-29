package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

/**
 * After submitting the email address, Okta displays a "Verify your email"
 * page containing a single "Send me an email" submit button to trigger the
 * delivery of the reset email.
 */
public class ResetFactorSelectionPage extends AbstractPage {

    @FindBy(xpath = "//h2[@data-se='o-form-head' and contains(text(),'Verify your email')]")
    private ExtendedWebElement verifyYourEmailHeader;

    // Locator must match the *enabled* Send me an email button on the
    // "Verify your email" page (not the disabled Next button left behind on
    // the previous Reset password page).
    @FindBy(xpath = "//button[normalize-space()='Send me an email' and not(@disabled)]")
    private ExtendedWebElement sendMeAnEmailButton;

    public ResetFactorSelectionPage(WebDriver driver) {
        super(driver);
    }

    public boolean isHeaderPresent() {
        return verifyYourEmailHeader.isElementPresent();
    }

    public boolean isSendMeAnEmailButtonPresent() {
        return sendMeAnEmailButton.isElementPresent();
    }

    public VerifyEmailPage clickSendMeAnEmail() {
        // Make sure we're on the right page (Verify your email) before clicking
        verifyYourEmailHeader.isElementPresent();
        sendMeAnEmailButton.click();
        return new VerifyEmailPage(getDriver());
    }

    public ExtendedWebElement getSendMeAnEmailButton() {
        return sendMeAnEmailButton;
    }

    public ExtendedWebElement getVerifyYourEmailHeader() {
        return verifyYourEmailHeader;
    }
}
