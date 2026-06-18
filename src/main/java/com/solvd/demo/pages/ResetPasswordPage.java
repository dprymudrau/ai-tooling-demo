package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class ResetPasswordPage extends AbstractPage {

    @FindBy(xpath = "//input[@name='identifier' or @id='input28' or @type='email']")
    private ExtendedWebElement emailAddressInput;

    @FindBy(xpath = "//input[@type='submit' and @value='Next'] | //button[normalize-space()='Next']")
    private ExtendedWebElement nextButton;

    public ResetPasswordPage(WebDriver driver) {
        super(driver);
    }

    public boolean isEmailAddressInputPresent() {
        return emailAddressInput.isPresent(15);
    }

    public boolean isNextButtonPresent() {
        return nextButton.isPresent(10);
    }

    public void enterEmail(String email) {
        emailAddressInput.type(email);
    }

    public String getEmailValue() {
        return emailAddressInput.getAttribute("value");
    }

    public ResetAuthenticatorPage clickNext() {
        nextButton.click();
        return new ResetAuthenticatorPage(getDriver());
    }
}
