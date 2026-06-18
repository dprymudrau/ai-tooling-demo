package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class ResetAuthenticatorPage extends AbstractPage {

    @FindBy(xpath = "//a[normalize-space()='Send me an email'] | //input[@type='submit' and @value='Send me an email'] | //button[normalize-space()='Send me an email']")
    private ExtendedWebElement sendMeAnEmailButton;

    public ResetAuthenticatorPage(WebDriver driver) {
        super(driver);
    }

    public boolean isSendMeAnEmailButtonPresent() {
        return sendMeAnEmailButton.isPresent(15);
    }

    public VerifyWithEmailPage clickSendMeAnEmail() {
        sendMeAnEmailButton.click();
        return new VerifyWithEmailPage(getDriver());
    }
}
