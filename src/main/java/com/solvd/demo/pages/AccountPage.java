package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AccountPage extends AbstractPage {

    private static final String ACCOUNT_URL = "https://www.underarmour.com/en-us/account/";

    @FindBy(xpath = "//*[contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'account')]")
    private ExtendedWebElement myAccountHeader;

    public AccountPage(WebDriver driver) {
        super(driver);
        setPageAbsoluteURL(ACCOUNT_URL);
    }

    public boolean isMyAccountHeaderPresent() {
        return myAccountHeader.isElementPresent(20);
    }

    public String getCurrentUrl() {
        return getDriver().getCurrentUrl();
    }

    public void navigateToAccountPage() {
        getDriver().get(ACCOUNT_URL);
        // Give the page time to render and dismiss any popup overlays
        new WebDriverWait(getDriver(), Duration.ofSeconds(30))
                .until(ExpectedConditions.urlContains("account"));
        dismissPopupIfPresent();
    }

    /**
     * Closes any marketing popups/modals overlapping the page content by removing them via JS.
     */
    public void dismissPopupIfPresent() {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        try {
            js.executeScript(
                    "var els = document.querySelectorAll(" +
                            "'[id*=\"popup\" i], [class*=\"popup\" i], [id*=\"modal\" i], [class*=\"modal\" i], [class*=\"overlay\" i], [aria-modal=\"true\"]'); " +
                            "for (var i=0; i<els.length; i++) { try { els[i].style.display='none'; } catch(e){} }"
            );
        } catch (Exception ignored) {
        }
    }
}
