package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.decorator.PageOpeningStrategy;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends AbstractPage {

    @FindBy(xpath = "//h2[normalize-space()='Log In']/ancestor::div[contains(@class,'card') or 1][1]")
    private ExtendedWebElement loginCardContainer;

    @FindBy(xpath = "//h1//img[contains(@alt,'us-web logo') or contains(@alt,'logo')]")
    private ExtendedWebElement uaLogo;

    @FindBy(xpath = "//h2[normalize-space()='Log In']")
    private ExtendedWebElement logInHeading;

    @FindBy(xpath = "//input[@id='identifier']")
    private ExtendedWebElement emailAddressInput;

    @FindBy(xpath = "//input[@id='credentials.passcode']")
    private ExtendedWebElement passwordInput;

    @FindBy(xpath = "//button[@aria-label='Show password']")
    private ExtendedWebElement showPasswordEyeIcon;

    @FindBy(xpath = "//label[@id='rememberMe-checkbox']")
    private ExtendedWebElement keepMeLoggedInCheckbox;

    @FindBy(xpath = "//button[normalize-space()='Forgot Password?']")
    private ExtendedWebElement forgotPasswordLink;

    @FindBy(xpath = "//button[@type='submit' and normalize-space()='Log In']")
    private ExtendedWebElement logInButton;

    @FindBy(xpath = "//button[normalize-space()='Continue as a Guest']")
    private ExtendedWebElement continueAsGuestButton;

    @FindBy(xpath = "//p[contains(normalize-space(),'New to Under Armour?')]")
    private ExtendedWebElement newToUnderArmourText;

    @FindBy(xpath = "//button[normalize-space()='Create Account']")
    private ExtendedWebElement createAccountLink;

    @FindBy(xpath = "//a[contains(normalize-space(),\"UA Rewards' Terms & Conditions\")]")
    private ExtendedWebElement uaRewardsTermsLink;

    @FindBy(xpath = "//a[normalize-space()='Privacy Policy']")
    private ExtendedWebElement privacyPolicyLink;

    @FindBy(xpath = "//a[normalize-space()='Terms & Conditions']")
    private ExtendedWebElement termsAndConditionsLink;

    public LoginPage(WebDriver driver) {
        super(driver);
        setPageAbsoluteURL("https://www.underarmour.com/en-us/login");
        // login URL redirects to login.shop.underarmour.com/oauth2/... — detect by element
        setPageOpeningStrategy(PageOpeningStrategy.BY_ELEMENT);
        setUiLoadedMarker(logInHeading);
    }

    public ExtendedWebElement getLoginCardContainer() {
        return loginCardContainer;
    }

    public ExtendedWebElement getUaLogo() {
        return uaLogo;
    }

    public ExtendedWebElement getLogInHeading() {
        return logInHeading;
    }

    public ExtendedWebElement getEmailAddressInput() {
        return emailAddressInput;
    }

    public ExtendedWebElement getPasswordInput() {
        return passwordInput;
    }

    public ExtendedWebElement getShowPasswordEyeIcon() {
        return showPasswordEyeIcon;
    }

    public ExtendedWebElement getKeepMeLoggedInCheckbox() {
        return keepMeLoggedInCheckbox;
    }

    public ExtendedWebElement getForgotPasswordLink() {
        return forgotPasswordLink;
    }

    public ExtendedWebElement getLogInButton() {
        return logInButton;
    }

    public ExtendedWebElement getContinueAsGuestButton() {
        return continueAsGuestButton;
    }

    public ExtendedWebElement getNewToUnderArmourText() {
        return newToUnderArmourText;
    }

    public ExtendedWebElement getCreateAccountLink() {
        return createAccountLink;
    }

    public ExtendedWebElement getUaRewardsTermsLink() {
        return uaRewardsTermsLink;
    }

    public ExtendedWebElement getPrivacyPolicyLink() {
        return privacyPolicyLink;
    }

    public ExtendedWebElement getTermsAndConditionsLink() {
        return termsAndConditionsLink;
    }
}
