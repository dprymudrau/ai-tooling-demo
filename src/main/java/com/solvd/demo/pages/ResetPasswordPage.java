package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

/**
 * Page where the user enters their email address to start the password
 * reset flow. Okta-hosted screen with the heading "Reset your password".
 */
public class ResetPasswordPage extends AbstractPage {

    @FindBy(xpath = "//h2[@data-se='o-form-head' and contains(text(),'Reset your password')]")
    private ExtendedWebElement resetPasswordHeader;

    @FindBy(xpath = "//input[@name='identifier' or @id='identifier' or @data-se='identifier']")
    private ExtendedWebElement emailAddressInput;

    // Once the email field is filled the Next submit button gets enabled. We
    // explicitly require not(@disabled) so we wait for the right state.
    @FindBy(xpath = "//button[@data-se='save' and @type='submit' and not(@disabled)]")
    private ExtendedWebElement nextButton;

    public ResetPasswordPage(WebDriver driver) {
        super(driver);
    }

    public boolean isResetPasswordHeaderPresent() {
        return resetPasswordHeader.isElementPresent();
    }

    public boolean isEmailFieldPresent() {
        // Wait for header first so we are sure the SPA has transitioned to
        // the reset password screen before checking the inputs.
        resetPasswordHeader.isElementPresent();
        return emailAddressInput.isElementPresent();
    }

    public boolean isNextButtonPresent() {
        return nextButton.isElementPresent();
    }

    public void typeEmail(String email) {
        // Ensure the Reset Password screen is rendered before interacting
        resetPasswordHeader.isElementPresent();
        emailAddressInput.type(email);
        // Press TAB to dismiss any virtual keyboard / autocomplete overlay
        emailAddressInput.getElement().sendKeys(Keys.TAB);
    }

    public ResetFactorSelectionPage clickNext() {
        nextButton.click();
        return new ResetFactorSelectionPage(getDriver());
    }

    public ExtendedWebElement getEmailAddressInput() {
        return emailAddressInput;
    }

    public ExtendedWebElement getNextButton() {
        return nextButton;
    }

    public ExtendedWebElement getResetPasswordHeader() {
        return resetPasswordHeader;
    }
}
