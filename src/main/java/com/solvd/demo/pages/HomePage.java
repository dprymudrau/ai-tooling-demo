package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class HomePage extends AbstractPage {

    public static final String HOME_PAGE_URL = "https://www.underarmour.com";

    @FindBy(id = "truste-consent-button")
    private ExtendedWebElement acceptCookiesButton;

    public HomePage(WebDriver driver) {
        super(driver);
        setPageAbsoluteURL(HOME_PAGE_URL);
    }

    public ExtendedWebElement getAcceptCookiesButton() {
        return acceptCookiesButton;
    }

    public void acceptCookies() {
        if (acceptCookiesButton.isPresent(10)) {
            acceptCookiesButton.click();
        }
    }
}
