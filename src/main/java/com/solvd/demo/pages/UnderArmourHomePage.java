package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class UnderArmourHomePage extends AbstractPage {

    public static final String HOME_URL = "https://www.underarmour.com/en-us/";

    @FindBy(id = "truste-consent-button")
    private ExtendedWebElement acceptCookiesButton;

    public UnderArmourHomePage(WebDriver driver) {
        super(driver);
        setPageAbsoluteURL(HOME_URL);
    }

    public boolean isAcceptCookiesButtonPresent() {
        return acceptCookiesButton.isElementPresent(15);
    }

    public void clickAcceptCookies() {
        if (isAcceptCookiesButtonPresent()) {
            acceptCookiesButton.click();
        }
    }
}
