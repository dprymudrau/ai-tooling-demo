package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class ArmourAccessSidePanel extends AbstractPage {

    @FindBy(xpath = "//*[@role='button'][normalize-space()='Log In']")
    private ExtendedWebElement logInButton;

    public ArmourAccessSidePanel(WebDriver driver) {
        super(driver);
    }

    public ExtendedWebElement getLogInButton() {
        return logInButton;
    }

    public LoginPage clickLogIn() {
        logInButton.click();
        return new LoginPage(getDriver());
    }
}
