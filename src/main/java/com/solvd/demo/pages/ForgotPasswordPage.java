package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ForgotPasswordPage extends AbstractPage {

    @FindBy(xpath = "//input[@name='identifier' or @type='email' or @type='text']")
    private ExtendedWebElement emailInputField;

    @FindBy(xpath = "//input[@type='submit'] | //button[@type='submit'] | //button[normalize-space()='Next'] | //a[normalize-space()='Next']")
    private ExtendedWebElement nextButton;

    public ForgotPasswordPage(WebDriver driver) {
        super(driver);
    }

    public boolean isEmailInputFieldPresent() {
        return emailInputField.isPresent(30);
    }

    public void typeEmail(String email) {
        // Wait for any dynamic content to finish loading
        waitForJSToLoad();
        WebElement element = emailInputField.getElement();

        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        // Scroll element into view and focus
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
        js.executeScript("arguments[0].focus();", element);

        try {
            // Try standard sendKeys first
            element.clear();
            element.sendKeys(email);
        } catch (Exception e) {
            // Fallback: set the value directly through JS and dispatch input/change events
            // so the framework (Okta) recognises the change.
            js.executeScript(
                    "var el = arguments[0]; var value = arguments[1]; " +
                            "var setter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set; " +
                            "setter.call(el, value); " +
                            "el.dispatchEvent(new Event('input', { bubbles: true })); " +
                            "el.dispatchEvent(new Event('change', { bubbles: true })); " +
                            "el.blur();",
                    element, email);
        }
    }

    public VerifyEmailPage clickNextButton() {
        waitForJSToLoad();
        nextButton.isPresent(30);
        WebElement element = nextButton.getElement();
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
        try {
            element.click();
        } catch (Exception e) {
            js.executeScript("arguments[0].click();", element);
        }
        return new VerifyEmailPage(getDriver());
    }
}
