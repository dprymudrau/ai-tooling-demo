package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class ResetPasswordPage extends AbstractPage {

    @FindBy(xpath = "//input[@type='email' or @name='identifier']")
    private ExtendedWebElement emailInput;

    @FindBy(xpath = "//input[@type='submit' and @value='Next'] | //button[normalize-space()='Next']")
    private ExtendedWebElement nextButton;

    public ResetPasswordPage(WebDriver driver) {
        super(driver);
    }

    public boolean isEmailInputPresent() {
        return emailInput.isElementPresent(30);
    }

    public boolean isNextButtonPresent() {
        return nextButton.isElementPresent(30);
    }

    public void typeEmail(String email) {
        emailInput.type(email);
        // dismiss virtual/soft keyboard if present (mobile/web view) by sending TAB
        emailInput.sendKeys(Keys.TAB);
    }

    public String getEmailFieldValue() {
        return emailInput.getAttribute("value");
    }

    public VerifyYourEmailPage clickNext() {
        nextButton.click();
        return new VerifyYourEmailPage(getDriver());
    }
}
