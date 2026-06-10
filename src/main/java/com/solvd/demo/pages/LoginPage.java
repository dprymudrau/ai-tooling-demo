package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends AbstractPage {

    @FindBy(xpath = "//a[normalize-space()='Forgot Password?'] | //button[normalize-space()='Forgot Password?']")
    private ExtendedWebElement forgotPasswordLink;

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
}
