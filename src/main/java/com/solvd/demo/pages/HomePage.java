package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class HomePage extends AbstractPage {

    @FindBy(id = "truste-consent-button")
    private ExtendedWebElement acceptCookiesButton;

    public HomePage(WebDriver driver) {
        super(driver);
        setPageAbsoluteURL("https://www.underarmour.com/en-us/");
    }

    public void acceptCookies() {
        if (acceptCookiesButton.isPresent(10)) {
            acceptCookiesButton.click();
        }
    }

    public boolean isAcceptCookiesButtonPresent() {
        return acceptCookiesButton.isPresent(10);
    }
}
