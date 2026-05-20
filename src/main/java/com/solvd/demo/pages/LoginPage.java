package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends AbstractPage {

    private static final String LOGIN_PAGE_URL = "https://www.underarmour.com/en-us/login";

    @FindBy(xpath = "//h2[normalize-space()='Log In']/ancestor::div[1]")
    private ExtendedWebElement loginCardContainer;

    @FindBy(xpath = "//img[@alt='us-web logo']")
    private ExtendedWebElement underArmourLogo;

    @FindBy(xpath = "//h2[normalize-space()='Log In']")
    private ExtendedWebElement logInTitle;

    @FindBy(xpath = "//input[@id='identifier']")
    private ExtendedWebElement emailAddressInput;

    @FindBy(xpath = "//label[normalize-space()='Email Address']")
    private ExtendedWebElement emailAddressLabel;

    @FindBy(xpath = "//input[@id='credentials.passcode' and @type='password']")
    private ExtendedWebElement passwordInput;

    @FindBy(xpath = "//label[normalize-space()='Password']")
    private ExtendedWebElement passwordLabel;

    @FindBy(xpath = "//button[@aria-label='Show password']")
    private ExtendedWebElement showPasswordToggleIcon;

    @FindBy(xpath = "//label[@id='rememberMe-checkbox']")
    private ExtendedWebElement keepMeLoggedInCheckbox;

    @FindBy(xpath = "//label[@id='rememberMe-checkbox']//input[@type='checkbox']")
    private ExtendedWebElement keepMeLoggedInCheckboxInput;

    @FindBy(xpath = "//button[normalize-space()='Forgot Password?']")
    private ExtendedWebElement forgotPasswordLink;

    @FindBy(xpath = "//button[@type='submit' and normalize-space()='Log In']")
    private ExtendedWebElement logInButton;

    @FindBy(xpath = "//button[normalize-space()='Continue as a Guest']")
    private ExtendedWebElement continueAsGuestButton;

    @FindBy(xpath = "//p[contains(.,'New to Under Armour?')]")
    private ExtendedWebElement newToUnderArmourText;

    @FindBy(xpath = "//button[normalize-space()='Create Account']")
    private ExtendedWebElement createAccountLink;

    @FindBy(xpath = "//a[normalize-space()=\"UA Rewards' Terms & Conditions\"]")
    private ExtendedWebElement uaRewardsTermsLink;

    @FindBy(xpath = "//a[normalize-space()='Privacy Policy']")
    private ExtendedWebElement privacyPolicyLink;

    @FindBy(xpath = "//a[normalize-space()='Terms & Conditions']")
    private ExtendedWebElement termsAndConditionsLink;

    public LoginPage(WebDriver driver) {
        super(driver);
        setPageAbsoluteURL(LOGIN_PAGE_URL);
    }

    public ExtendedWebElement getLoginCardContainer() {
        return loginCardContainer;
    }

    public ExtendedWebElement getUnderArmourLogo() {
        return underArmourLogo;
    }

    public ExtendedWebElement getLogInTitle() {
        return logInTitle;
    }

    public ExtendedWebElement getEmailAddressInput() {
        return emailAddressInput;
    }

    public ExtendedWebElement getEmailAddressLabel() {
        return emailAddressLabel;
    }

    public ExtendedWebElement getPasswordInput() {
        return passwordInput;
    }

    public ExtendedWebElement getPasswordLabel() {
        return passwordLabel;
    }

    public ExtendedWebElement getShowPasswordToggleIcon() {
        return showPasswordToggleIcon;
    }

    public ExtendedWebElement getKeepMeLoggedInCheckbox() {
        return keepMeLoggedInCheckbox;
    }

    public ExtendedWebElement getKeepMeLoggedInCheckboxInput() {
        return keepMeLoggedInCheckboxInput;
    }

    public boolean isKeepMeLoggedInCheckboxChecked() {
        return keepMeLoggedInCheckboxInput.getElement().isSelected();
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
