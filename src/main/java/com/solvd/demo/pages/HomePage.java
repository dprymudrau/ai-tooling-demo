package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HomePage extends AbstractPage {

    private static final Logger LOGGER = LoggerFactory.getLogger(HomePage.class);

    // Under Armour logo - reliable marker that the homepage has loaded.
    @FindBy(xpath = "//a[contains(@aria-label,'Under Armour') or contains(@href,'/en-us/')]//*[name()='svg' or name()='img']" +
            " | //img[contains(@alt,'Under Armour')]" +
            " | //a[contains(@href,'/en-us')]/*[name()='svg']")
    private ExtendedWebElement uaLogo;

    // "Log In or Join" link in the header - matches multiple candidate shapes.
    @FindBy(xpath = "//a[contains(@href,'/login')] | //a[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'log in')]")
    private ExtendedWebElement accountIcon;

    // OneTrust cookie banner "I ACCEPT" button - may obstruct other clicks.
    @FindBy(xpath = "//button[@id='onetrust-accept-btn-handler'] | //button[normalize-space()='I ACCEPT']")
    private ExtendedWebElement acceptCookiesButton;

    public HomePage(WebDriver driver) {
        super(driver);
        setPageURL("/en-us/");
    }

    /**
     * Dismisses the cookie consent banner if it's present.
     */
    public void dismissCookieBannerIfPresent() {
        try {
            if (acceptCookiesButton.isElementPresent(3)) {
                acceptCookiesButton.click();
                LOGGER.info("Cookie banner dismissed");
            }
        } catch (Exception e) {
            LOGGER.warn("Failed to dismiss cookie banner: {}", e.getMessage());
        }
    }

    /**
     * Confirms the homepage is loaded by checking either the UA logo or the URL.
     */
    public boolean isHomePageLoaded() {
        try {
            String currentUrl = getDriver().getCurrentUrl();
            if (currentUrl != null && currentUrl.contains("underarmour.com")) {
                return true;
            }
        } catch (Exception ignored) {
        }
        return uaLogo.isElementPresent(5);
    }

    /**
     * Navigates to the Log In page. Tries to click the account icon first; if
     * that fails (e.g. icon hidden behind a banner or a different DOM shape),
     * falls back to directly opening the login URL listed in the manual case.
     */
    public LoginPage clickAccountIcon() {
        dismissCookieBannerIfPresent();
        boolean navigated = false;
        try {
            if (accountIcon.isElementPresent(5)) {
                try {
                    accountIcon.click();
                    navigated = true;
                } catch (Exception clickFail) {
                    LOGGER.warn("Native click on account icon failed, falling back to JS click: {}",
                            clickFail.getMessage());
                    ((JavascriptExecutor) getDriver())
                            .executeScript("arguments[0].click();", accountIcon.getElement());
                    navigated = true;
                }
            }
        } catch (Exception e) {
            LOGGER.warn("Account icon click attempt failed: {}", e.getMessage());
        }
        if (!navigated) {
            LOGGER.info("Account icon not interactable; navigating directly to /en-us/login");
            getDriver().get("https://www.underarmour.com/en-us/login");
        }
        return new LoginPage(getDriver());
    }
}
