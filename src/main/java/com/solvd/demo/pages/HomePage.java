package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class HomePage extends AbstractPage {

    private static final Logger LOGGER = LoggerFactory.getLogger(HomePage.class);

    public static final String HOME_URL = "https://www.underarmour.com/en-us/";
    public static final String LOGIN_URL = "https://www.underarmour.com/en-us/login";

    @FindBy(xpath = "//a[contains(@href,'/login') or @data-testid='login-link']")
    private ExtendedWebElement loginLink;

    public HomePage(WebDriver driver) {
        super(driver);
        setPageAbsoluteURL(HOME_URL);
    }

    /**
     * Navigate to the heavy Under Armour homepage with a short page load
     * timeout. The home page pulls many third-party assets we don't need;
     * we time out fast and call window.stop() to release the DOM.
     */
    public void navigate() {
        try {
            getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));
        } catch (Exception ignored) {
        }
        try {
            getDriver().get(HOME_URL);
        } catch (TimeoutException timeout) {
            LOGGER.warn("pageLoadTimeout while loading {}: {}", HOME_URL, timeout.getMessage());
        }
        try {
            ((JavascriptExecutor) getDriver()).executeScript("window.stop();");
        } catch (Exception ignored) {
        }
    }

    public boolean isHomeUrlOpened() {
        String current = getDriver().getCurrentUrl();
        return current != null && current.contains("underarmour.com");
    }

    /**
     * Navigate to the login URL. The /login URL performs a client-side
     * redirect to the Okta-hosted OAuth provider
     * (login.shop.underarmour.com). We use a longer page load timeout so the
     * Okta SPA has time to initialize and we do NOT call window.stop() here
     * because doing so prevents the SPA from finishing rendering.
     */
    public LoginPage openLoginPage() {
        try {
            getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(45));
        } catch (Exception ignored) {
        }
        try {
            getDriver().get(LOGIN_URL);
        } catch (TimeoutException timeout) {
            LOGGER.warn("pageLoadTimeout while loading {}: {}", LOGIN_URL, timeout.getMessage());
        }
        String cur = getDriver().getCurrentUrl();
        LOGGER.info("openLoginPage: current URL after load = {}", cur);
        return new LoginPage(getDriver());
    }

    public ExtendedWebElement getLoginLink() {
        return loginLink;
    }
}
