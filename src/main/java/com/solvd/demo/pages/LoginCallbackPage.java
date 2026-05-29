package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class LoginCallbackPage extends AbstractPage {

    @FindBy(xpath = "//*[contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'logging you in')]")
    private ExtendedWebElement loggingYouInText;

    public LoginCallbackPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoggingYouInTextPresent() {
        return loggingYouInText.isElementPresent();
    }

    public String getLoggingYouInText() {
        return loggingYouInText.getText();
    }

    public ExtendedWebElement getLoggingYouInTextElement() {
        return loggingYouInText;
    }
}
