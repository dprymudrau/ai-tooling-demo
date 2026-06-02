package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class ResetPasswordPage extends AbstractPage {

    // Under Armour logo - rendered as <img> on the Okta widget.
    @FindBy(xpath = "//img[contains(@alt,'logo') or contains(@alt,'Under Armour') or contains(@src,'logo')]")
    private ExtendedWebElement underArmourLogo;

    // Heading - "Reset your password". Okta widget uses h2 or h1 depending on version.
    @FindBy(xpath = "//h1[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'reset your password')]" +
            " | //h2[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'reset your password')]" +
            " | //div[@role='heading' and contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'reset your password')]")
    private ExtendedWebElement resetYourPasswordHeading;

    // New password field - first password input.
    @FindBy(xpath = "(//input[@type='password'])[1]")
    private ExtendedWebElement newPasswordField;

    // Re-enter password - second password input.
    @FindBy(xpath = "(//input[@type='password'])[2]")
    private ExtendedWebElement reEnterPasswordField;

    // Requirements list - any list/section that mentions character requirements.
    @FindBy(xpath = "//ul[contains(@class,'password-requirements')]" +
            " | //*[contains(@class,'password-requirements')]" +
            " | //*[contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'characters') and (self::ul or self::ol or self::div)]")
    private ExtendedWebElement requirementsList;

    // Reset Password submit button.
    @FindBy(xpath = "//button[normalize-space()='Reset Password']" +
            " | //input[@type='submit' and (@value='Reset Password' or contains(@value,'Reset'))]" +
            " | //button[@type='submit']")
    private ExtendedWebElement resetPasswordButton;

    // Back to Log In link/button.
    @FindBy(xpath = "//a[normalize-space()='Back to Log In']" +
            " | //button[normalize-space()='Back to Log In']" +
            " | //*[normalize-space(text())='Back to Log In']")
    private ExtendedWebElement backToLogInLink;

    public ResetPasswordPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLogoPresent() {
        return underArmourLogo.isElementPresent(10);
    }

    public boolean isResetHeadingPresent() {
        return resetYourPasswordHeading.isElementPresent(10);
    }

    /**
     * Returns the heading text or an empty string if the heading isn't found.
     * The non-throwing variant is used so it can be called inside soft asserts.
     */
    public String getResetHeadingTextSafe() {
        try {
            if (resetYourPasswordHeading.isElementPresent(5)) {
                return resetYourPasswordHeading.getText();
            }
        } catch (Exception ignored) {
        }
        return "";
    }

    public boolean isNewPasswordFieldPresent() {
        return newPasswordField.isElementPresent(10);
    }

    public boolean isReEnterPasswordFieldPresent() {
        return reEnterPasswordField.isElementPresent(10);
    }

    public boolean isRequirementsListPresent() {
        return requirementsList.isElementPresent(10);
    }

    public boolean isResetPasswordButtonPresent() {
        return resetPasswordButton.isElementPresent(10);
    }

    public boolean isBackToLogInLinkPresent() {
        return backToLogInLink.isElementPresent(10);
    }
}
