package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends AbstractPage {

    private static final String LOGIN_URL = "https://www.underarmour.com/en-us/login";

    @FindBy(xpath = "//form | //div[.//input[@type='password']]")
    private ExtendedWebElement loginFormContainer;

    @FindBy(xpath = "//input[@type='email' or @name='email' or @id='email' or @name='identifier' or @id='identifier' or @autocomplete='username' or @autocomplete='email' or @inputmode='email']")
    private ExtendedWebElement emailInput;

    @FindBy(xpath = "//input[@type='password' or @name='password' or @id='password' or @autocomplete='current-password']")
    private ExtendedWebElement passwordInput;

    @FindBy(xpath = "//button[normalize-space()='Log In' or normalize-space()='Sign In' or @type='submit' or @data-testid='login-submit']")
    private ExtendedWebElement logInSubmitButton;

    public LoginPage(WebDriver driver) {
        super(driver);
        setPageAbsoluteURL(LOGIN_URL);
    }

    public boolean isLoginFormPresent() {
        return loginFormContainer.isElementPresent(30);
    }

    public boolean isEmailInputPresent() {
        return emailInput.isElementPresent(30);
    }

    public void typeEmail(String email) {
        // Wait for the field to be available
        emailInput.isElementPresent(30);
        WebElement el = emailInput.getElement();
        // Use JS to set value and trigger change events because the page might block direct typing
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript(
                "arguments[0].focus();" +
                "var setter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;" +
                "setter.call(arguments[0], arguments[1]);" +
                "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
                "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                el, email);
        // Lose focus to dismiss keyboard / trigger validation
        el.sendKeys(Keys.TAB);
    }

    public void typePassword(String password) {
        passwordInput.isElementPresent(30);
        WebElement el = passwordInput.getElement();
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript(
                "arguments[0].focus();" +
                "var setter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;" +
                "setter.call(arguments[0], arguments[1]);" +
                "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
                "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                el, password);
        // Lose focus to dismiss keyboard / trigger validation
        el.sendKeys(Keys.TAB);
    }

    public AccountPage clickLogIn() {
        logInSubmitButton.click();
        return new AccountPage(getDriver());
    }
}
