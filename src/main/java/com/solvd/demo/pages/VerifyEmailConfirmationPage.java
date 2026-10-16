package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class VerifyEmailConfirmationPage extends AbstractPage {

    @FindBy(xpath = "//h2[contains(.,'Verify with your email')]")
    private ExtendedWebElement verifyWithYourEmailHeading;

    public VerifyEmailConfirmationPage(WebDriver driver) {
        super(driver);
    }

    public boolean isVerifyWithYourEmailHeadingPresent() {
        return verifyWithYourEmailHeading.isPresent(30);
    }

    public String getVerifyWithYourEmailHeadingText() {
        return verifyWithYourEmailHeading.getText();
    }
}
