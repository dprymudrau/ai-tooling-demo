package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.decorator.PageOpeningStrategy;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends AbstractPage {

    @FindBy(xpath = "//a[normalize-space()='Forgot Password?'] | //button[normalize-space()='Forgot Password?']")
    private ExtendedWebElement forgotPasswordLink;

    public LoginPage(WebDriver driver) {
        super(driver);
        // The /login URL redirects to an Okta OAuth domain, so we identify the login page by
        // the presence of the 'Forgot Password?' link rather than by URL.
        setPageAbsoluteURL("https://www.underarmour.com/en-us/login");
        setUiLoadedMarker(forgotPasswordLink);
        setPageOpeningStrategy(PageOpeningStrategy.BY_ELEMENT);
    }

    public boolean isForgotPasswordLinkPresent() {
        return forgotPasswordLink.isElementPresent(30);
    }

    public ResetPasswordPage clickForgotPasswordLink() {
        forgotPasswordLink.click();
        return new ResetPasswordPage(getDriver());
    }
}
