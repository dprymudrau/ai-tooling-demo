package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class ResetPasswordPage extends AbstractPage {

    @FindBy(xpath = "//input[@type='email' or @name='identifier']")
    private ExtendedWebElement emailInputField;

    @FindBy(xpath = "//input[@type='submit' and @value='Next'] | //button[normalize-space()='Next']")
    private ExtendedWebElement nextButton;

    public ResetPasswordPage(WebDriver driver) {
        super(driver);
    }

    public boolean isEmailInputFieldPresent() {
        return emailInputField.isElementPresent(15);
    }

    public boolean isNextButtonPresent() {
        return nextButton.isElementPresent(15);
    }

    public void enterEmail(String email) {
        emailInputField.type(email);
        // handle keyboard hiding so the Next button is clickable
        emailInputField.sendKeys(Keys.TAB);
    }

    public VerifyEmailPage clickNextButton() {
        nextButton.click();
        return new VerifyEmailPage(getDriver());
    }
}
