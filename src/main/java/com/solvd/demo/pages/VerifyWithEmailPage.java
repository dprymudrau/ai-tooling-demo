package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class VerifyWithEmailPage extends AbstractPage {

    @FindBy(xpath = "//a[normalize-space()='Send me an email'] | //button[normalize-space()='Send me an email']")
    private ExtendedWebElement sendMeAnEmailButton;

    public VerifyWithEmailPage(WebDriver driver) {
        super(driver);
    }

    public void clickSendMeAnEmail() {
        sendMeAnEmailButton.click();
    }

    public boolean isSendMeAnEmailButtonPresent() {
        return sendMeAnEmailButton.isElementPresent();
    }
}
