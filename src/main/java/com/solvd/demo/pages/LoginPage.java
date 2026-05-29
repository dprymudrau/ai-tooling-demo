package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends AbstractPage {

    @FindBy(xpath = "//input[@name='identifier' or @id='identifier' or @data-se='identifier']")
    private ExtendedWebElement emailField;

    @FindBy(xpath = "//input[@name='credentials.passcode' or @data-se='credentials.passcode']")
    private ExtendedWebElement passwordField;

    @FindBy(xpath = "//button[@data-se='save' and @type='submit']")
    private ExtendedWebElement loginButton;

    @FindBy(xpath = "//button[@data-se='forgot-password'] | //a[normalize-space()='Forgot Password?']")
    private ExtendedWebElement forgotPasswordLink;

    public LoginPage(WebDriver driver) {
        super(driver);
        setPageAbsoluteURL("https://www.underarmour.com/en-us/login");
    }

    public boolean isForgotPasswordLinkPresent() {
        return forgotPasswordLink.isElementPresent();
    }

    public boolean isEmailFieldPresent() {
        return emailField.isElementPresent();
    }

    public ResetPasswordPage clickForgotPassword() {
        forgotPasswordLink.click();
        return new ResetPasswordPage(getDriver());
    }

    public ExtendedWebElement getEmailField() {
        return emailField;
    }

    public ExtendedWebElement getPasswordField() {
        return passwordField;
    }

    public ExtendedWebElement getLoginButton() {
        return loginButton;
    }

    public ExtendedWebElement getForgotPasswordLink() {
        return forgotPasswordLink;
    }
}
