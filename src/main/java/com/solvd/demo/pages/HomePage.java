package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class HomePage extends AbstractPage {

    private static final String HOME_URL = "https://www.underarmour.com/en-us/";

    @FindBy(id = "truste-consent-button")
    private ExtendedWebElement acceptCookiesButton;

    @FindBy(xpath = "//button[@id='reward-header-switcher'] | //a[contains(@href,'/login')]")
    private ExtendedWebElement loginOrJoinLink;

    public HomePage(WebDriver driver) {
        super(driver);
        setPageAbsoluteURL(HOME_URL);
    }

    public void acceptCookies() {
        if (acceptCookiesButton.isElementPresent(10)) {
            acceptCookiesButton.click();
        }
    }

    public boolean isAcceptCookiesButtonPresent() {
        return acceptCookiesButton.isElementPresent(10);
    }

    public LoginPage clickLogInOrJoin() {
        loginOrJoinLink.click();
        return new LoginPage(getDriver());
    }
}
