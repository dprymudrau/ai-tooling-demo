package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.decorator.PageOpeningStrategy;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage extends AbstractPage {

    @FindBy(xpath = "//a[normalize-space()='Forgot Password?'] | //a[contains(@href,'forgot-password')] | //button[normalize-space()='Forgot Password?']")
    private ExtendedWebElement forgotPasswordLink;

    public LoginPage(WebDriver driver) {
        super(driver);
        setPageAbsoluteURL("https://www.underarmour.com/en-us/login");
        // The Under Armour login URL redirects to login.shop.underarmour.com, so we use the
        // forgot password link as the page-opened marker rather than relying on the URL.
        setPageOpeningStrategy(PageOpeningStrategy.BY_ELEMENT);
        setUiLoadedMarker(forgotPasswordLink);
    }

    public boolean isForgotPasswordLinkPresent() {
        return forgotPasswordLink.isPresent(30);
    }

    public ForgotPasswordPage clickForgotPasswordLink() {
        waitForJSToLoad();
        forgotPasswordLink.isPresent(30);
        WebElement element = forgotPasswordLink.getElement();
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", element);

        String urlBefore = getDriver().getCurrentUrl();
        try {
            element.click();
        } catch (StaleElementReferenceException stale) {
            // Element has gone stale because the page navigated. Treat as success.
        } catch (Exception e) {
            try {
                js.executeScript("arguments[0].click();", element);
            } catch (StaleElementReferenceException ignored) {
                // navigation already happened
            }
        }

        // Wait for the page to actually transition to the reset/forgot password screen.
        try {
            new WebDriverWait(getDriver(), Duration.ofSeconds(30))
                    .until(ExpectedConditions.or(
                            ExpectedConditions.urlContains("forgot-password"),
                            ExpectedConditions.urlContains("reset"),
                            ExpectedConditions.not(ExpectedConditions.urlToBe(urlBefore)),
                            ExpectedConditions.visibilityOfElementLocated(
                                    org.openqa.selenium.By.xpath(
                                            "//h1[normalize-space()='Reset your password'] | //h2[normalize-space()='Reset your password']"))
                    ));
        } catch (Exception ignored) {
            // Last resort - reset page may still be loading; downstream assertions will fail loudly.
        }
        return new ForgotPasswordPage(getDriver());
    }
}
