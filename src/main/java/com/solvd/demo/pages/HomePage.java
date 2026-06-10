package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage extends AbstractPage {

    // First (desktop) instance of the account switcher button (aria-label="Log In", text "My Account|0 pts")
    @FindBy(xpath = "(//button[@data-testid='my-account-switcher'])[1]")
    private ExtendedWebElement logInOrJoinButton;

    // First (desktop) instance of the Log In link inside the dropdown
    @FindBy(xpath = "(//a[@data-testid='login-btn'])[1]")
    private ExtendedWebElement logInDropdownLink;

    public HomePage(WebDriver driver) {
        super(driver);
        setPageAbsoluteURL("https://www.underarmour.com/en-us/");
    }

    public ExtendedWebElement getLogInOrJoinButton() {
        return logInOrJoinButton;
    }

    public ExtendedWebElement getLogInDropdownLink() {
        return logInDropdownLink;
    }

    /**
     * Checks for DOM presence (visibility-agnostic) via JS - hidden header dropdown items
     * still count as present.
     */
    public boolean isLogInOrJoinButtonInDom() {
        Object result = ((JavascriptExecutor) getDriver()).executeScript(
                "return !!document.querySelector('button[data-testid=\"my-account-switcher\"]');"
        );
        return Boolean.TRUE.equals(result);
    }

    public boolean isLogInDropdownLinkInDom() {
        Object result = ((JavascriptExecutor) getDriver()).executeScript(
                "return !!document.querySelector('a[data-testid=\"login-btn\"]');"
        );
        return Boolean.TRUE.equals(result);
    }

    /**
     * Closes any currently open marketing/SMS dialog by JS so it doesn't intercept clicks.
     */
    private void dismissMarketingDialogs() {
        try {
            ((JavascriptExecutor) getDriver()).executeScript(
                    "document.querySelectorAll('dialog[open]').forEach(function(d){" +
                            "  var cb = d.querySelector('button[data-testid=\"dialog-close-button\"]');" +
                            "  if (cb) { cb.click(); }" +
                            "  try { d.close(); } catch (e) {}" +
                            "});"
            );
        } catch (Exception ignored) {
            // best-effort cleanup
        }
    }

    public void clickLogInOrJoin() {
        dismissMarketingDialogs();
        // Use JS to click — works even if a transient dialog still overlaps
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript(
                "var b = document.querySelector('[data-testid=\"header-account\"] button[data-testid=\"my-account-switcher\"]') " +
                        "|| document.querySelector('button[data-testid=\"my-account-switcher\"]');" +
                        "if (b) { b.scrollIntoView({block:'center'}); b.click(); }"
        );
    }

    public LoginPage openLoginPage() {
        clickLogInOrJoin();

        // Click the Log In link in the dropdown via JS (it may be present but visually hidden depending on rewards state)
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript(
                "var l = document.querySelector('a[data-testid=\"login-btn\"]');" +
                        "if (l) { l.click(); }"
        );

        // Wait for the OAuth-protected login page to load
        new WebDriverWait(getDriver(), Duration.ofSeconds(60))
                .until(ExpectedConditions.urlContains("login.shop.underarmour.com"));
        // Wait for the Log In heading to be rendered
        new WebDriverWait(getDriver(), Duration.ofSeconds(60))
                .until(ExpectedConditions.titleContains("Log In"));

        return new LoginPage(getDriver());
    }
}
