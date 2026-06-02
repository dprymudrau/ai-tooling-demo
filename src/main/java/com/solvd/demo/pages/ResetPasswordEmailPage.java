package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class ResetPasswordEmailPage extends AbstractPage {

    @FindBy(xpath = "//input[@name='identifier' or @type='email']")
    private ExtendedWebElement emailInput;

    @FindBy(xpath = "//input[@type='submit' and @value='Next'] | //button[normalize-space()='Next']")
    private ExtendedWebElement nextButton;

    public ResetPasswordEmailPage(WebDriver driver) {
        super(driver);
    }

    public void typeEmail(String email) {
        emailInput.type(email);
        // handle on-screen keyboard / dismiss any popups by sending TAB after input
        emailInput.sendKeys(Keys.TAB);
    }

    public VerifyWithEmailPage clickNext() {
        nextButton.click();
        return new VerifyWithEmailPage(getDriver());
    }

    public boolean isEmailInputPresent() {
        return emailInput.isElementPresent();
    }

    public boolean isNextButtonPresent() {
        return nextButton.isElementPresent();
    }
}
