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
    private ExtendedWebElement logInOrJoinButton;

    @FindBy(xpath = "//a[@data-testid='login-btn']")
    private ExtendedWebElement logInDropdownLink;

    public HomePage(WebDriver driver) {
        super(driver);
        setPageAbsoluteURL("https://www.underarmour.com/en-us/");
    }

    public void acceptCookies() {
        if (acceptCookiesButton.isPresent(10)) {
            acceptCookiesButton.click();
        }
    }

    public boolean isAcceptCookiesButtonPresent() {
        return acceptCookiesButton.isPresent(10);
    }

    public boolean isLogInOrJoinButtonPresent() {
        return logInOrJoinButton.isPresent(15);
    }

    /**
     * The homepage intermittently shows a marketing pop-up (SMS subscription dialog) that overlays the
     * header and intercepts clicks. Remove any such dialog/backdrop so header interactions are reliable.
     */
    public void dismissOverlays() {
        try {
            ((JavascriptExecutor) getDriver()).executeScript(
                    "document.querySelectorAll('dialog[data-testid=\"dialog-base\"]').forEach(function(d){"
                            + "try{d.close();}catch(e){} d.remove();});"
                            + "document.querySelectorAll('[class*=\"backdrop\"],[class*=\"Backdrop\"]')"
                            + ".forEach(function(b){b.remove();});"
                            + "if(document.body){document.body.style.overflow='auto';}");
        } catch (Exception ignored) {
            // best-effort cleanup; ignore if scripting is unavailable
        }
    }

    public void clickLogInOrJoin() {
        logInOrJoinButton.click();
    }

    public boolean isLogInDropdownLinkPresent() {
        return logInDropdownLink.isPresent(15);
    }

    public LoginPage clickLogInInDropdown() {
        logInDropdownLink.click();
        return new LoginPage(getDriver());
    }
}
