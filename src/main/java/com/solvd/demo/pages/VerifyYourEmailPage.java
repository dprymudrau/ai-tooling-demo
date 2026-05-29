package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class VerifyYourEmailPage extends AbstractPage {

    @FindBy(xpath = "//a[normalize-space()='Send me an email'] | //button[normalize-space()='Send me an email']")
    private ExtendedWebElement sendMeAnEmailButton;

    public VerifyYourEmailPage(WebDriver driver) {
        super(driver);
    }

    public boolean isSendMeAnEmailButtonPresent() {
        return sendMeAnEmailButton.isElementPresent(30);
    }

    public VerifyWithYourEmailPage clickSendMeAnEmail() {
        sendMeAnEmailButton.click();
        return new VerifyWithYourEmailPage(getDriver());
    }
}
