package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class ResetPasswordPage extends AbstractPage {

    @FindBy(xpath = "//input[@type='email' or @name='identifier' or @id='input28']")
    private ExtendedWebElement emailInput;

    @FindBy(xpath = "//input[@type='submit' and @value='Next'] | //button[normalize-space()='Next']")
    private ExtendedWebElement nextButton;

    public ResetPasswordPage(WebDriver driver) {
        super(driver);
    }

    public boolean isEmailInputPresent() {
        return emailInput.isPresent(20);
    }

    public boolean isNextButtonPresent() {
        return nextButton.isPresent(20);
    }

    public void enterEmail(String email) {
        emailInput.type(email);
        // Handle keyboard hiding by pressing TAB to defocus input field
        emailInput.sendKeys(Keys.TAB);
    }

    public String getEmailValue() {
        return emailInput.getAttribute("value");
    }

    public VerifyEmailPage clickNext() {
        nextButton.click();
        return new VerifyEmailPage(getDriver());
    }
}
