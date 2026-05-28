package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class HomePage extends AbstractPage {

    @FindBy(id = "truste-consent-button")
    private ExtendedWebElement acceptCookiesButton;

    @FindBy(id = "reward-header-switcher")
    private ExtendedWebElement loginOrJoinButton;

    @FindBy(xpath = "//a[@data-testid='login-btn' and normalize-space()='Log In']")
    private ExtendedWebElement logInDropdownButton;

    public HomePage(WebDriver driver) {
        super(driver);
        setPageAbsoluteURL("https://www.underarmour.com/en-us/");
    }

    public void acceptCookies() {
        acceptCookiesButton.clickIfPresent();
    }

    /**
     * Close email subscription popup / promotional overlays that intercept header clicks.
     * Uses JS to dismiss anything with class/data-testid that hints at a close/dismiss control,
     * and presses the ESC key as a fallback.
     */
    public void dismissPromoOverlays() {
        try {
            ((JavascriptExecutor) getDriver()).executeScript(
                "const sels = ['button[aria-label=\"Close\"]','button[aria-label=\"close\"]',"
                    + "'[data-testid*=\"close\"]','[class*=\"close-button\"]','[class*=\"CloseButton\"]',"
                    + "'[class*=\"close-icon\"]','button[class*=\"close\" i]','svg[aria-label*=\"close\" i]'];"
                    + "sels.forEach(s => document.querySelectorAll(s).forEach(el => { try { el.click(); } catch(e){} }));"
            );
        } catch (Exception ignored) {
            // best-effort cleanup
        }
    }

    public void clickLoginOrJoinIcon() {
        // Use JS click to bypass any promo overlays that may intercept the click
        try {
            ((JavascriptExecutor) getDriver()).executeScript(
                "const el = document.getElementById('reward-header-switcher'); if (el) el.click();"
            );
        } catch (Exception e) {
            loginOrJoinButton.click();
        }
    }

    public LoginPage clickLogInFromDropdown() {
        // Click the visible Log In link in the account dropdown; fall back to JS for any overlay
        try {
            ((JavascriptExecutor) getDriver()).executeScript(
                "const btns = document.querySelectorAll('a[data-testid=\"login-btn\"]');"
                    + "for (const b of btns) { if (b.offsetWidth > 0) { b.click(); return; } }"
                    + "if (btns.length) btns[0].click();"
            );
        } catch (Exception e) {
            logInDropdownButton.click();
        }
        return new LoginPage(getDriver());
    }

    public boolean isAcceptCookiesButtonPresent() {
        return acceptCookiesButton.isPresent();
    }

    public boolean isLoginOrJoinButtonPresent() {
        return loginOrJoinButton.isPresent();
    }

    public boolean isLogInDropdownButtonPresent() {
        return logInDropdownButton.isPresent();
    }
}
