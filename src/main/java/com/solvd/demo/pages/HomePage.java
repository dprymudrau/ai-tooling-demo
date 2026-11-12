package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class HomePage extends AbstractPage {

    @FindBy(xpath = "//button[contains(., 'Log In or Join')]")
    private ExtendedWebElement logInOrJoinControl;

    public HomePage(WebDriver driver) {
        super(driver);
        setPageAbsoluteURL("https://www.underarmour.com/en-us/");
    }

    public ArmourAccessSidePanel openLogInOrJoinPanel() {
        logInOrJoinControl.click();
        return new ArmourAccessSidePanel(getDriver());
    }
}
