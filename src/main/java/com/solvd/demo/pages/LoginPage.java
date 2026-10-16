package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends AbstractPage {

    @FindBy(xpath = "//a[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'forgot password')] | //button[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'forgot password')]")
    private ExtendedWebElement forgotPasswordLink;

    public LoginPage(WebDriver driver) {
        super(driver);
        setPageURL("/login");
    }

    public boolean isForgotPasswordLinkPresent() {
        return forgotPasswordLink.isPresent(20);
    }

    public ResetPasswordPage clickForgotPasswordLink() {
        forgotPasswordLink.click();
        return new ResetPasswordPage(getDriver());
    }
}
