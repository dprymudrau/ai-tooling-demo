package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends AbstractPage {

    @FindBy(xpath = "//h2[normalize-space(text())='Log In']/ancestor::form")
    private ExtendedWebElement loginCardContainer;

    @FindBy(xpath = "//h1//img[contains(@alt,'us-web logo')]")
    private ExtendedWebElement uaLogo;

    @FindBy(xpath = "//h2[normalize-space(text())='Log In']")
    private ExtendedWebElement logInHeading;

    @FindBy(xpath = "//input[@id='identifier']")
    private ExtendedWebElement emailAddressInput;

    @FindBy(xpath = "//input[@id='credentials.passcode']")
    private ExtendedWebElement passwordInput;

    @FindBy(xpath = "//button[@aria-label='Show password']")
    private ExtendedWebElement showPasswordToggleButton;

    @FindBy(xpath = "//label[@id='rememberMe-checkbox']")
    private ExtendedWebElement keepMeLoggedInCheckbox;

    @FindBy(xpath = "//label[@id='rememberMe-checkbox']//input[@type='checkbox']")
    private ExtendedWebElement keepMeLoggedInCheckboxInput;

    @FindBy(xpath = "//button[normalize-space(text())='Forgot Password?']")
    private ExtendedWebElement forgotPasswordLink;

    @FindBy(xpath = "//button[@type='submit' and normalize-space(.)='Log In']")
    private ExtendedWebElement logInSubmitButton;

    @FindBy(xpath = "//button[normalize-space(text())='Continue as a Guest']")
    private ExtendedWebElement continueAsGuestButton;

    @FindBy(xpath = "//p[contains(normalize-space(.),'New to Under Armour?')]")
    private ExtendedWebElement newToUnderArmourText;

    @FindBy(xpath = "//button[normalize-space(text())='Create Account']")
    private ExtendedWebElement createAccountLink;

    @FindBy(xpath = "//a[normalize-space(text())=\"UA Rewards' Terms & Conditions\"]")
    private ExtendedWebElement uaRewardsTermsLink;

    @FindBy(xpath = "//a[normalize-space(text())='Privacy Policy']")
    private ExtendedWebElement privacyPolicyLink;

    @FindBy(xpath = "//a[normalize-space(text())='Terms & Conditions']")
    private ExtendedWebElement termsAndConditionsLink;

    public LoginPage(WebDriver driver) {
        super(driver);
        setPageAbsoluteURL("https://www.underarmour.com/en-us/login");
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

    public ExtendedWebElement getShowPasswordToggleButton() {
        return showPasswordToggleButton;
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
