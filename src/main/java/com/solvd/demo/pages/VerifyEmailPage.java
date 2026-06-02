package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class VerifyEmailPage extends AbstractPage {

    @FindBy(xpath = "//a[normalize-space()='Send me an email'] | //button[normalize-space()='Send me an email']")
    private ExtendedWebElement sendMeAnEmailButton;

    public VerifyEmailPage(WebDriver driver) {
        super(driver);
    }

    public boolean isSendMeAnEmailButtonPresent() {
        return sendMeAnEmailButton.isElementPresent(20);
    }

    public VerifyEmailConfirmationPage clickSendMeAnEmailButton() {
        sendMeAnEmailButton.click();
        return new VerifyEmailConfirmationPage(getDriver());
    }
}
