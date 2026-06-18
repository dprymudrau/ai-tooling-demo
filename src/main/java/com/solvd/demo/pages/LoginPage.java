package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends AbstractPage {

    @FindBy(xpath = "//a[normalize-space()='Forgot Password?'] | //button[normalize-space()='Forgot Password?']")
    private ExtendedWebElement forgotPasswordLink;

    @FindBy(id = "identifier")
    private ExtendedWebElement emailAddressInput;

    @FindBy(id = "credentials.passcode")
    private ExtendedWebElement passwordInput;

    @FindBy(xpath = "//label[@id='rememberMe-checkbox']//input[@type='checkbox']")
    private ExtendedWebElement keepMeLoggedInCheckbox;

    @FindBy(xpath = "//button[@type='submit' and normalize-space()='Log In']")
    private ExtendedWebElement logInSubmitButton;

    @FindBy(xpath = "//button[normalize-space(.)='Continue as a Guest']")
    private ExtendedWebElement continueAsGuestButton;

    @FindBy(xpath = "//button[normalize-space(.)='Create Account']")
    private ExtendedWebElement createAccountLink;

    @FindBy(xpath = "//*[contains(text(),'We found some errors. Please review the form and make corrections.')]")
    private ExtendedWebElement globalErrorBanner;

    @FindBy(xpath = "(//*[contains(text(),'This field cannot be left blank.')])[1]")
    private ExtendedWebElement emailInlineError;

    @FindBy(xpath = "(//*[contains(text(),'This field cannot be left blank.')])[2]")
    private ExtendedWebElement passwordInlineError;

    @FindBy(xpath = "//*[contains(text(),'Incorrect password or account not found')]")
    private ExtendedWebElement incorrectPasswordErrorBanner;

    // --- Email step-up verification ("Verify with your email") ---
    @FindBy(xpath = "//*[normalize-space()='Verify with your email']")
    private ExtendedWebElement verifyWithEmailHeading;

    @FindBy(xpath = "//a[normalize-space()='Enter a verification code instead']"
            + " | //button[normalize-space()='Enter a verification code instead']")
    private ExtendedWebElement enterCodeInsteadLink;

    @FindBy(id = "credentials.passcode")
    private ExtendedWebElement verificationCodeInput;

    @FindBy(xpath = "//button[@type='submit' and normalize-space()='Verify']")
    private ExtendedWebElement verifyButton;

    public LoginPage(WebDriver driver) {
        super(driver);
        setPageAbsoluteURL("https://www.underarmour.com/en-us/login");
    }

    public boolean isForgotPasswordLinkPresent() {
        return forgotPasswordLink.isPresent(15);
    }

    public ResetPasswordPage clickForgotPassword() {
        forgotPasswordLink.click();
        return new ResetPasswordPage(getDriver());
    }

    public boolean isEmailAddressInputPresent() {
        return emailAddressInput.isPresent(20);
    }

    public boolean isPasswordInputPresent() {
        return passwordInput.isPresent(15);
    }

    public boolean isLogInSubmitButtonPresent() {
        return logInSubmitButton.isPresent(15);
    }

    public void enterEmail(String email) {
        emailAddressInput.type(email);
        // hide virtual keyboard / blur the field after typing
        emailAddressInput.sendKeys(Keys.TAB);
    }

    public String getEmailValue() {
        return emailAddressInput.getAttribute("value");
    }

    public void enterPassword(String password) {
        passwordInput.type(password);
        passwordInput.sendKeys(Keys.TAB);
    }

    public boolean isKeepMeLoggedInCheckboxPresent() {
        return keepMeLoggedInCheckbox.isPresent(15);
    }

    public boolean isKeepMeLoggedInChecked() {
        return keepMeLoggedInCheckbox.isChecked();
    }

    public void clickLogInSubmit() {
        logInSubmitButton.click();
    }

    public String getPasswordValue() {
        return passwordInput.getAttribute("value");
    }

    public boolean isGlobalErrorBannerPresent() {
        return globalErrorBanner.isPresent(15);
    }

    public String getGlobalErrorBannerText() {
        return globalErrorBanner.getText();
    }

    public boolean isEmailInlineErrorPresent() {
        return emailInlineError.isPresent(15);
    }

    public String getEmailInlineErrorText() {
        return emailInlineError.getText();
    }

    public boolean isPasswordInlineErrorPresent() {
        return passwordInlineError.isPresent(15);
    }

    public String getPasswordInlineErrorText() {
        return passwordInlineError.getText();
    }

    public String getEmailInvalidState() {
        return emailAddressInput.getAttribute("aria-invalid");
    }

    public String getPasswordInvalidState() {
        return passwordInput.getAttribute("aria-invalid");
    }

    public boolean isKeepMeLoggedInCheckboxInputChecked() {
        return keepMeLoggedInCheckbox.isChecked();
    }

    /**
     * The Okta login form only flags a required field as invalid after it has received an input
     * (value-change) event and lost focus. To reproduce the documented "both fields empty" behaviour
     * (global banner + inline error on BOTH fields), we touch each field: type a character, delete it
     * (fires input events leaving the field empty) and blur it. A plain submit alone only flags the
     * password field.
     */
    public void triggerEmptyFieldsValidation() {
        emailAddressInput.click();
        emailAddressInput.type("a");
        emailAddressInput.sendKeys(Keys.BACK_SPACE);
        emailAddressInput.sendKeys(Keys.TAB);

        passwordInput.click();
        passwordInput.type("a");
        passwordInput.sendKeys(Keys.BACK_SPACE);
        passwordInput.sendKeys(Keys.TAB);
    }

    public boolean isForgotPasswordButtonPresent() {
        return forgotPasswordLink.isPresent(15);
    }

    public boolean isCreateAccountLinkPresent() {
        return createAccountLink.isPresent(15);
    }

    public boolean isLogInSubmitButtonEnabled() {
        return logInSubmitButton.isEnabled();
    }

    public boolean isContinueAsGuestButtonEnabled() {
        return continueAsGuestButton.isEnabled();
    }

    public boolean isIncorrectPasswordErrorBannerPresent() {
        return incorrectPasswordErrorBanner.isPresent(20);
    }

    public String getIncorrectPasswordErrorBannerText() {
        return incorrectPasswordErrorBanner.getText();
    }

    public boolean isVerifyWithEmailPagePresent(long timeoutSeconds) {
        return verifyWithEmailHeading.isPresent(timeoutSeconds);
    }

    public void clickEnterCodeInstead() {
        if (enterCodeInsteadLink.isPresent(10)) {
            enterCodeInsteadLink.click();
        }
    }

    public void enterVerificationCode(String code) {
        verificationCodeInput.type(code);
    }

    public void clickVerify() {
        verifyButton.click();
    }
}
