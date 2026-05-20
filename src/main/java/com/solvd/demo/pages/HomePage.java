package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.decorator.PageOpeningStrategy;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class HomePage extends AbstractPage {

    @FindBy(xpath = "//*[@id='reward-header-switch'] | //*[normalize-space()='Log in or Join'] | //*[normalize-space()='Log In or Join'] | //a[contains(@href,'login') and (contains(.,'Log in') or contains(.,'Log In'))]")
    private ExtendedWebElement logInOrJoinButton;

    @FindBy(xpath = "//button[normalize-space()='Log In'] | //a[normalize-space()='Log In'] | //button[normalize-space()='Log in'] | //a[normalize-space()='Log in']")
    private ExtendedWebElement logInDropdownOption;

    @FindBy(id = "onetrust-accept-btn-handler")
    private ExtendedWebElement acceptCookiesButton;

    public HomePage(WebDriver driver) {
        super(driver);
        setPageURL("/");
        setPageOpeningStrategy(PageOpeningStrategy.BY_ELEMENT);
        setUiLoadedMarker(logInOrJoinButton);
    }

    public ExtendedWebElement getLogInOrJoinButton() {
        return logInOrJoinButton;
    }

    public ExtendedWebElement getLogInDropdownOption() {
        return logInDropdownOption;
    }

    public void acceptCookiesIfPresent() {
        if (acceptCookiesButton.isElementPresent(3)) {
            acceptCookiesButton.click();
        }
    }

    public void clickLogInOrJoinButton() {
        acceptCookiesIfPresent();
        logInOrJoinButton.click();
    }

    public LoginPage clickLogInOption() {
        logInDropdownOption.click();
        return new LoginPage(getDriver());
    }
}
