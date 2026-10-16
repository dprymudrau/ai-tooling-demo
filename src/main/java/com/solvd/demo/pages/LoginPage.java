package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends AbstractPage {

    // On the Okta-hosted login the "Forgot password?" is a submit button (not anchor)
    @FindBy(xpath = "//button[normalize-space(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'))='forgot password?'] | //a[normalize-space(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'))='forgot password?']")
    private ExtendedWebElement forgotPasswordLink;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public ResetPasswordPage clickForgotPassword() {
        try {
            ((JavascriptExecutor) getDriver()).executeScript(
                "const all = document.querySelectorAll('button, a');"
                    + "for (const el of all) { const t = (el.innerText || '').toLowerCase().trim();"
                    + " if (t === 'forgot password?' && el.offsetWidth > 0) { el.click(); return; } }"
            );
        } catch (Exception e) {
            forgotPasswordLink.click();
        }
        return new ResetPasswordPage(getDriver());
    }

    public boolean isForgotPasswordLinkPresent() {
        return forgotPasswordLink.isPresent();
    }
}
