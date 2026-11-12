package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends AbstractPage {

    @FindBy(xpath = "//h2[normalize-space()='Log In']/ancestor::form | //h2[normalize-space()='Log In']")
    private ExtendedWebElement loginFormCard;

    @FindBy(xpath = "//h1")
    private ExtendedWebElement armourAccessLogo;

    @FindBy(xpath = "//h2[normalize-space()='Log In']")
    private ExtendedWebElement logInHeading;

    @FindBy(id = "identifier")
    private ExtendedWebElement emailAddressField;

    @FindBy(id = "credentials.passcode")
    private ExtendedWebElement passwordField;

    @FindBy(xpath = "//button[@type='submit'][normalize-space()='Log In']")
    private ExtendedWebElement logInSubmitButton;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public ExtendedWebElement getLoginFormCard() {
        return loginFormCard;
    }

    public ExtendedWebElement getArmourAccessLogo() {
        return armourAccessLogo;
    }

    public ExtendedWebElement getLogInHeading() {
        return logInHeading;
    }

    public ExtendedWebElement getEmailAddressField() {
        return emailAddressField;
    }

    public ExtendedWebElement getPasswordField() {
        return passwordField;
    }

    public ExtendedWebElement getLogInSubmitButton() {
        return logInSubmitButton;
    }
}
