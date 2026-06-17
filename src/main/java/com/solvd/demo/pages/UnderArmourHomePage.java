package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class UnderArmourHomePage extends AbstractPage {

    @FindBy(id = "truste-consent-button")
    private ExtendedWebElement acceptCookiesButton;

    // Note: Real id on the live site is 'reward-header-switcher' (the test case
    // value 'reward-header-switch' is a typo - confirmed via live DOM inspection)
    @FindBy(id = "reward-header-switcher")
    private ExtendedWebElement logInOrJoinHeaderButton;

    // Scope the 'Log In' link to the dropdown menu that opens when the header
    // button is clicked. There are multiple 'Log In' elements on the page so
    // we must constrain it to the dropdown.
    @FindBy(xpath = "//div[@id='reward-header-dropdown']//a[normalize-space(.)='Log In']")
    private ExtendedWebElement logInDropdownLink;

    // Elements for the change-location interstitial (shown when selenium is
    // running from a non-US IP, e.g. inside a Docker container).
    @FindBy(xpath = "//*[@data-target='North_America']")
    private ExtendedWebElement northAmericaAccordionHeader;

    @FindBy(xpath = "//a[normalize-space(.)='United States']")
    private ExtendedWebElement unitedStatesLink;

    public UnderArmourHomePage(WebDriver driver) {
        super(driver);
        setPageAbsoluteURL("https://www.underarmour.com/en-us/");
    }

    /**
     * If the site redirected the browser to the /en-us/change-location/ page
     * (happens for non-US IPs), expand the North America accordion and click
     * "United States" to reach the actual homepage.
     */
    public void handleChangeLocationIfNeeded() {
        String currentUrl = getDriver().getCurrentUrl();
        if (currentUrl != null && currentUrl.contains("/change-location")) {
            if (northAmericaAccordionHeader.isElementPresent(15)) {
                ((JavascriptExecutor) getDriver()).executeScript(
                        "arguments[0].click();", northAmericaAccordionHeader.getElement());
            }
            if (unitedStatesLink.isElementPresent(15)) {
                ((JavascriptExecutor) getDriver()).executeScript(
                        "arguments[0].click();", unitedStatesLink.getElement());
            }
            // After click, the homepage starts loading
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void acceptCookies() {
        if (acceptCookiesButton.isElementPresent(20)) {
            try {
                acceptCookiesButton.click();
            } catch (Exception ex) {
                // Fall back to JS click in case overlay intercepts pointer events.
                ((JavascriptExecutor) getDriver()).executeScript(
                        "arguments[0].click();", acceptCookiesButton.getElement());
            }
        }
    }

    public void clickLogInOrJoin() {
        // The header button can be hidden behind a sticky overlay; use a JS
        // click for reliability while still relying on the standard locator
        // for presence.
        logInOrJoinHeaderButton.isElementPresent(30);
        ((JavascriptExecutor) getDriver()).executeScript(
                "arguments[0].click();", logInOrJoinHeaderButton.getElement());
    }

    public LoginPage openLoginPage() {
        logInDropdownLink.isElementPresent(30);
        ((JavascriptExecutor) getDriver()).executeScript(
                "arguments[0].click();", logInDropdownLink.getElement());
        return new LoginPage(getDriver());
    }
}
