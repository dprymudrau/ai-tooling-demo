package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

/**
 * Form opened by following the password reset email link in the same browser
 * session that initiated the reset. Okta names the inputs
 * "credentials.passcode" (new password) and "confirmPassword".
 */
public class ResetPasswordFormPage extends AbstractPage {

    @FindBy(xpath = "//input[@name='credentials.passcode' or @data-se='credentials.passcode' or @id='credentials.passcode']")
    private ExtendedWebElement newPasswordInput;

    @FindBy(xpath = "//input[@name='confirmPassword' or @data-se='confirmPassword' or @id='confirmPassword']")
    private ExtendedWebElement reEnterPasswordInput;

    @FindBy(xpath = "//button[@data-se='save' and @type='submit'] | //button[normalize-space()='Reset Password']")
    private ExtendedWebElement resetPasswordButton;

    @FindBy(xpath = "//h2[@data-se='o-form-head']")
    private ExtendedWebElement formHeader;

    public ResetPasswordFormPage(WebDriver driver) {
        super(driver);
    }

    public boolean isNewPasswordFieldPresent() {
        return newPasswordInput.isElementPresent();
    }

    public boolean isReEnterPasswordFieldPresent() {
        return reEnterPasswordInput.isElementPresent();
    }

    public boolean isResetButtonPresent() {
        return resetPasswordButton.isElementPresent();
    }

    public void typeNewPassword(String password) {
        newPasswordInput.type(password);
        newPasswordInput.getElement().sendKeys(Keys.TAB);
    }

    public void typeReEnterPassword(String password) {
        reEnterPasswordInput.type(password);
        reEnterPasswordInput.getElement().sendKeys(Keys.TAB);
    }

    public LoginCallbackPage clickResetPassword() {
        resetPasswordButton.click();
        return new LoginCallbackPage(getDriver());
    }

    public ExtendedWebElement getNewPasswordInput() {
        return newPasswordInput;
    }

    public ExtendedWebElement getReEnterPasswordInput() {
        return reEnterPasswordInput;
    }

    public ExtendedWebElement getResetPasswordButton() {
        return resetPasswordButton;
    }

    public ExtendedWebElement getFormHeader() {
        return formHeader;
    }
}
