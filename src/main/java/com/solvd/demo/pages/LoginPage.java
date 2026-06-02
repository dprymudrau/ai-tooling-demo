package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends AbstractPage {

    private static final String LOGIN_URL = "https://www.underarmour.com/en-us/login";

    @FindBy(xpath = "//a[normalize-space()='Forgot Password?'] | //button[normalize-space()='Forgot Password?']")
    private ExtendedWebElement forgotPasswordLink;

    public LoginPage(WebDriver driver) {
        super(driver);
        setPageAbsoluteURL(LOGIN_URL);
    }

    public boolean isForgotPasswordLinkPresent() {
        return forgotPasswordLink.isElementPresent(15);
    }

    public ResetPasswordPage clickForgotPasswordLink() {
        forgotPasswordLink.click();
        return new ResetPasswordPage(getDriver());
    }
}
