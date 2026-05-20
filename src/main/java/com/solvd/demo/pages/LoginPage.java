package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends AbstractPage {

    // Lenient locator: the real UA login card is a generic div ancestor that
    // also contains the Log In submit button (no `card`/`panel` class).
    @FindBy(xpath = "(//h2[normalize-space()='Log In']/ancestor::div[.//button[@type='submit' and normalize-space()='Log In']])[1]")
    private ExtendedWebElement loginCardContainer;

    @FindBy(xpath = "//h1//img[contains(@alt,'us-web logo') or contains(@alt,'logo')]")
    private ExtendedWebElement uaLogo;

    @FindBy(xpath = "//h2[normalize-space()='Log In']")
    private ExtendedWebElement logInHeading;

    @FindBy(xpath = "//input[@id='identifier']")
    private ExtendedWebElement emailAddressInput;

    @FindBy(xpath = "//label[normalize-space()='Email Address']")
    private ExtendedWebElement emailAddressFloatingLabel;

    @FindBy(xpath = "//input[@id='credentials.passcode']")
    private ExtendedWebElement passwordInput;

    @FindBy(xpath = "//label[normalize-space()='Password']")
    private ExtendedWebElement passwordFloatingLabel;

    @FindBy(xpath = "//button[@aria-label='Show password']")
    private ExtendedWebElement showHidePasswordEyeIcon;

    @FindBy(xpath = "//label[@id='rememberMe-checkbox']//input[@type='checkbox'] | //label[@id='rememberMe-checkbox']")
    private ExtendedWebElement keepMeLoggedInCheckbox;

    @FindBy(xpath = "//label[@id='rememberMe-checkbox']//input[@type='checkbox']")
    private ExtendedWebElement keepMeLoggedInCheckboxInput;

    @FindBy(xpath = "//button[normalize-space()='Forgot Password?']")
    private ExtendedWebElement forgotPasswordLink;

    @FindBy(xpath = "//button[@type='submit' and normalize-space()='Log In']")
    private ExtendedWebElement logInSubmitButton;

    @FindBy(xpath = "//button[normalize-space()='Continue as a Guest']")
    private ExtendedWebElement continueAsGuestButton;

    @FindBy(xpath = "//p[contains(normalize-space(),'New to Under Armour?')]")
    private ExtendedWebElement newToUnderArmourText;

    @FindBy(xpath = "//button[normalize-space()='Create Account']")
    private ExtendedWebElement createAccountLink;

    @FindBy(xpath = "//a[normalize-space()=\"UA Rewards' Terms & Conditions\"]")
    private ExtendedWebElement uaRewardsTermsConditionsLink;

    @FindBy(xpath = "//a[normalize-space()='Privacy Policy']")
    private ExtendedWebElement privacyPolicyLink;

    @FindBy(xpath = "//a[normalize-space()='Terms & Conditions']")
    private ExtendedWebElement termsConditionsLink;

    public LoginPage(WebDriver driver) {
        super(driver);
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

    public ExtendedWebElement getEmailAddressFloatingLabel() {
        return emailAddressFloatingLabel;
    }

    public ExtendedWebElement getPasswordInput() {
        return passwordInput;
    }

    public ExtendedWebElement getPasswordFloatingLabel() {
        return passwordFloatingLabel;
    }

    public ExtendedWebElement getShowHidePasswordEyeIcon() {
        return showHidePasswordEyeIcon;
    }

    public ExtendedWebElement getKeepMeLoggedInCheckbox() {
        return keepMeLoggedInCheckbox;
    }

    public ExtendedWebElement getKeepMeLoggedInCheckboxInput() {
        return keepMeLoggedInCheckboxInput;
    }

    public ExtendedWebElement getForgotPasswordLink() {
        return forgotPasswordLink;
    }

    public ExtendedWebElement getLogInSubmitButton() {
        return logInSubmitButton;
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

    public ExtendedWebElement getUaRewardsTermsConditionsLink() {
        return uaRewardsTermsConditionsLink;
    }

    public ExtendedWebElement getPrivacyPolicyLink() {
        return privacyPolicyLink;
    }

    public ExtendedWebElement getTermsConditionsLink() {
        return termsConditionsLink;
    }
}
