package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class UnderArmourHomePage extends AbstractPage {

    @FindBy(id = "truste-consent-button")
    private ExtendedWebElement acceptCookiesButton;

    public UnderArmourHomePage(WebDriver driver) {
        super(driver);
        setPageAbsoluteURL("https://www.underarmour.com/en-us/");
    }

    public void acceptCookiesIfDisplayed() {
        if (acceptCookiesButton.isElementPresent(10)) {
            acceptCookiesButton.click();
        }
    }

    public ExtendedWebElement getAcceptCookiesButton() {
        return acceptCookiesButton;
    }
}
