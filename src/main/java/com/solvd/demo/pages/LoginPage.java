package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginPage extends AbstractPage {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginPage.class);

    // Forgot Password link/button - matches a, button, or span (the Okta widget
    // sometimes renders it as a non-anchor element).
    @FindBy(xpath = "//a[normalize-space()='Forgot Password?']" +
            " | //button[normalize-space()='Forgot Password?']" +
            " | //span[normalize-space()='Forgot Password?']" +
            " | //*[normalize-space(text())='Forgot Password?']")
    private ExtendedWebElement forgotPasswordLink;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public ResetPasswordEmailPage clickForgotPassword() {
        try {
            forgotPasswordLink.click();
        } catch (Exception e) {
            LOGGER.warn("Native click on Forgot Password failed, falling back to JS click: {}",
                    e.getMessage());
            ((JavascriptExecutor) getDriver())
                    .executeScript("arguments[0].click();", forgotPasswordLink.getElement());
        }
        return new ResetPasswordEmailPage(getDriver());
    }

    public boolean isForgotPasswordLinkPresent() {
        return forgotPasswordLink.isElementPresent(10);
    }
}
