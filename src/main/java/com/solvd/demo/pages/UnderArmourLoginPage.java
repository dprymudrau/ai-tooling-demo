package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class UnderArmourLoginPage extends AbstractPage {

    public static final String LOGIN_URL = "https://www.underarmour.com/en-us/login";

    @FindBy(xpath = "//h2[normalize-space()='Log In']")
    private ExtendedWebElement logInHeading;

    @FindBy(xpath = "//input[@id='identifier']")
    private ExtendedWebElement emailAddressInputField;

    @FindBy(xpath = "//input[@id='credentials.passcode']")
    private ExtendedWebElement passwordInputField;

    @FindBy(xpath = "//button[@aria-label='Show password']")
    private ExtendedWebElement showPasswordToggleButton;

    @FindBy(xpath = "//label[@id='rememberMe-checkbox']")
    private ExtendedWebElement keepMeLoggedInCheckbox;

    @FindBy(xpath = "//button[normalize-space()='Forgot Password?']")
    private ExtendedWebElement forgotPasswordLink;

    @FindBy(xpath = "//button[@type='submit' and normalize-space()='Log In']")
    private ExtendedWebElement logInButton;

    @FindBy(xpath = "//button[normalize-space()='Continue as a Guest']")
    private ExtendedWebElement continueAsGuestButton;

    @FindBy(xpath = "//p[normalize-space()='New to Under Armour?']")
    private ExtendedWebElement newToUnderArmourText;

    @FindBy(xpath = "//button[normalize-space()='Create Account']")
    private ExtendedWebElement createAccountLink;

    @FindBy(xpath = "//a[normalize-space()=\"UA Rewards' Terms & Conditions\"]")
    private ExtendedWebElement uaRewardsTermsAndConditionsLink;

    @FindBy(xpath = "//a[normalize-space()='Privacy Policy']")
    private ExtendedWebElement privacyPolicyLink;

    @FindBy(xpath = "//a[normalize-space()='Terms & Conditions']")
    private ExtendedWebElement termsAndConditionsLink;

    public UnderArmourLoginPage(WebDriver driver) {
        super(driver);
        setPageAbsoluteURL(LOGIN_URL);
    }

    public ExtendedWebElement getLogInHeading() {
        return logInHeading;
    }

    public ExtendedWebElement getEmailAddressInputField() {
        return emailAddressInputField;
    }

    public ExtendedWebElement getPasswordInputField() {
        return passwordInputField;
    }

    public ExtendedWebElement getShowPasswordToggleButton() {
        return showPasswordToggleButton;
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

    public ExtendedWebElement getUaRewardsTermsAndConditionsLink() {
        return uaRewardsTermsAndConditionsLink;
    }

    public ExtendedWebElement getPrivacyPolicyLink() {
        return privacyPolicyLink;
    }

    public ExtendedWebElement getTermsAndConditionsLink() {
        return termsAndConditionsLink;
    }
}
