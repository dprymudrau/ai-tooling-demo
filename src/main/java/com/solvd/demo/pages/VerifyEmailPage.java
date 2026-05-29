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

/**
 * Confirmation page shown after Okta sent the password reset email.
 */
public class VerifyEmailPage extends AbstractPage {

    private static final Logger LOGGER = LoggerFactory.getLogger(VerifyEmailPage.class);

    @FindBy(xpath = "//h2[@data-se='o-form-head'] | //h1 | //h2")
    private ExtendedWebElement headerElement;

    @FindBy(xpath = "//*[contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'sent') and contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'email')]")
    private ExtendedWebElement confirmationMessage;

    public VerifyEmailPage(WebDriver driver) {
        super(driver);
    }

    public boolean isHeaderPresent() {
        return headerElement.isElementPresent();
    }

    public ExtendedWebElement getHeaderElement() {
        return headerElement;
    }

    public ExtendedWebElement getConfirmationMessage() {
        return confirmationMessage;
    }

    /**
     * Navigate the current driver session to the reset URL from the email.
     * The link must be opened in the same browser session that initiated
     * the reset request so that Okta recognizes the cookies and shows the
     * reset password form directly (instead of an email-OTP verification
     * page).
     */
    public ResetPasswordFormPage openResetLink(String resetUrl) {
        LOGGER.info("Opening reset link: {}", resetUrl);
        try {
            getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        } catch (Exception ignored) {
        }
        try {
            getDriver().get(resetUrl);
        } catch (TimeoutException timeout) {
            LOGGER.warn("pageLoadTimeout while loading reset URL: {}", timeout.getMessage());
        }
        // Do NOT call window.stop() here. Some Okta same-session detection
        // relies on a tiny in-page script running after DOMContentLoaded.
        // Stopping the page can leave us on a generic verification screen
        // instead of the password reset form.
        LOGGER.info("After reset link load - URL: {} | Title: {}",
                getDriver().getCurrentUrl(),
                getDriver().getTitle());
        return new ResetPasswordFormPage(getDriver());
    }
}
