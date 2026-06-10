package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.decorator.PageOpeningStrategy;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends AbstractPage {

    @FindBy(xpath = "//h1//img[contains(@alt,'us-web logo')]")
    private ExtendedWebElement uaLogo;

    @FindBy(xpath = "//h2[normalize-space()='Log In']")
    private ExtendedWebElement logInTitle;

    @FindBy(xpath = "//input[@id='identifier']")
    private ExtendedWebElement emailAddressInput;

    @FindBy(xpath = "//label[normalize-space()='Email Address']")
    private ExtendedWebElement emailAddressLabel;

    @FindBy(xpath = "//input[@id='credentials.passcode']")
    private ExtendedWebElement passwordInput;

    @FindBy(xpath = "//label[normalize-space()='Password']")
    private ExtendedWebElement passwordLabel;

    @FindBy(xpath = "//button[@aria-label='Show password']")
    private ExtendedWebElement showPasswordEyeIcon;

    @FindBy(xpath = "//label[@id='rememberMe-checkbox']//span[normalize-space()='Keep me logged in']")
    private ExtendedWebElement keepMeLoggedInCheckboxLabel;

    @FindBy(xpath = "//label[@id='rememberMe-checkbox']//input[@type='checkbox']")
    private ExtendedWebElement keepMeLoggedInCheckboxInput;

    @FindBy(xpath = "//button[@role='link' and normalize-space()='Forgot Password?']")
    private ExtendedWebElement forgotPasswordLink;

    @FindBy(xpath = "//button[@type='submit' and normalize-space()='Log In']")
    private ExtendedWebElement logInSubmitButton;

    @FindBy(xpath = "//button[normalize-space()='Continue as a Guest']")
    private ExtendedWebElement continueAsGuestButton;

    @FindBy(xpath = "//p[contains(normalize-space(),'New to Under Armour?')]")
    private ExtendedWebElement newToUnderArmourText;

    @FindBy(xpath = "//button[@role='link' and normalize-space()='Create Account']")
    private ExtendedWebElement createAccountLink;

    @FindBy(xpath = "//a[normalize-space()=\"UA Rewards' Terms & Conditions\"]")
    private ExtendedWebElement uaRewardsTermsLink;

    @FindBy(xpath = "//a[normalize-space()='Privacy Policy']")
    private ExtendedWebElement privacyPolicyLink;

    @FindBy(xpath = "//a[normalize-space()='Terms & Conditions']")
    private ExtendedWebElement termsAndConditionsLink;

    public LoginPage(WebDriver driver) {
        super(driver);
        setPageOpeningStrategy(PageOpeningStrategy.BY_ELEMENT);
        setUiLoadedMarker(logInTitle);
    }

    public ExtendedWebElement getUaLogo() {
        return uaLogo;
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

    public ExtendedWebElement getShowPasswordEyeIcon() {
        return showPasswordEyeIcon;
    }

    public ExtendedWebElement getKeepMeLoggedInCheckboxLabel() {
        return keepMeLoggedInCheckboxLabel;
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

    public boolean isKeepMeLoggedInCheckboxUnchecked() {
        return !keepMeLoggedInCheckboxInput.getElement().isSelected();
    }
}
