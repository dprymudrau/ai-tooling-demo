package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class HomePage extends AbstractPage {

    private static final String HOME_URL = "https://www.underarmour.com/en-us/";

    @FindBy(id = "truste-consent-button")
    private ExtendedWebElement acceptCookiesButton;

    public HomePage(WebDriver driver) {
        super(driver);
        setPageAbsoluteURL(HOME_URL);
    }

    public void openHomePage() {
        open();
    }

    public boolean isAcceptCookiesButtonPresent() {
        return acceptCookiesButton.isElementPresent(15);
    }

    public void acceptCookies() {
        if (isAcceptCookiesButtonPresent()) {
            acceptCookiesButton.click();
        }
    }

    public LoginPage navigateToLoginPage() {
        getDriver().get("https://www.underarmour.com/en-us/login");
        return new LoginPage(getDriver());
    }
}
