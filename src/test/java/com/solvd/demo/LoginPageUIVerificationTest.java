package com.solvd.demo;

import com.solvd.demo.pages.HomePage;
import com.solvd.demo.pages.LoginPage;
import com.zebrunner.agent.core.annotation.TestCaseKey;
import com.zebrunner.carina.core.IAbstractTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class LoginPageUIVerificationTest implements IAbstractTest {

    @Test(description = "Verify Login page loads with all UI elements visible")
    @TestCaseKey("PIL-159")
    public void verifyLoginPageLoadsWithAllUIElementsVisible() {
        // Step 0: Open Under Armour home page
        HomePage homePage = new HomePage(getDriver());
        homePage.open();
        homePage.assertPageOpened();

        // Step 1: Accept cookies if banner appears
        homePage.acceptCookiesIfPresent();

        // Step 2: Navigate to Login page
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.open();
        loginPage.assertPageOpened();

        SoftAssert softAssert = new SoftAssert();

        // Step 3: Verify UA Logo and Log In heading
        softAssert.assertTrue(loginPage.getUaLogo().isElementPresent(),
                "UA Logo is not displayed on the Login Page.");
        softAssert.assertTrue(loginPage.getLogInHeading().isElementPresent(),
                "Log In heading is not displayed on the Login Page.");
        softAssert.assertEquals(loginPage.getLogInHeading().getText().trim(), "Log In",
                "Log In heading text does not match expected value.");

        // Step 4: Verify form fields - Email, Password, Show Password Toggle
        softAssert.assertTrue(loginPage.getEmailAddressInput().isElementPresent(),
                "Email Address input field is not displayed.");
        softAssert.assertTrue(loginPage.getPasswordInput().isElementPresent(),
                "Password input field is not displayed.");
        softAssert.assertTrue(loginPage.getShowPasswordToggle().isElementPresent(),
                "Show Password toggle (eye icon) is not displayed.");

        // Step 5: Verify Keep Me Logged In checkbox and Forgot Password link
        softAssert.assertTrue(loginPage.getKeepMeLoggedInCheckbox().isElementPresent(),
                "'Keep me logged in' checkbox is not displayed.");
        softAssert.assertFalse(loginPage.getKeepMeLoggedInCheckboxInput().getElement().isSelected(),
                "'Keep me logged in' checkbox should not be checked by default.");
        softAssert.assertTrue(loginPage.getForgotPasswordLink().isElementPresent(),
                "'Forgot Password?' link is not displayed.");

        // Step 6: Verify Log In and Continue as Guest buttons
        softAssert.assertTrue(loginPage.getLogInButton().isElementPresent(),
                "Primary 'Log In' button is not displayed.");
        softAssert.assertTrue(loginPage.getLogInButton().getElement().isEnabled(),
                "Primary 'Log In' button is not enabled.");
        softAssert.assertTrue(loginPage.getContinueAsGuestButton().isElementPresent(),
                "'Continue as a Guest' button is not displayed.");
        softAssert.assertTrue(loginPage.getContinueAsGuestButton().getElement().isEnabled(),
                "'Continue as a Guest' button is not enabled.");

        // Step 7: Verify footer - New to UA text, Create Account link, legal disclaimer links
        softAssert.assertTrue(loginPage.getNewToUnderArmourText().isElementPresent(),
                "'New to Under Armour?' text is not displayed.");
        softAssert.assertTrue(loginPage.getNewToUnderArmourText().getText().contains("New to Under Armour?"),
                "'New to Under Armour?' text does not match expected value.");
        softAssert.assertTrue(loginPage.getCreateAccountLink().isElementPresent(),
                "'Create Account' link is not displayed.");
        softAssert.assertTrue(loginPage.getUaRewardsTermsLink().isElementPresent(),
                "'UA Rewards' Terms & Conditions' link is not displayed.");
        softAssert.assertTrue(loginPage.getPrivacyPolicyLink().isElementPresent(),
                "'Privacy Policy' link is not displayed.");
        softAssert.assertTrue(loginPage.getTermsConditionsLink().isElementPresent(),
                "'Terms & Conditions' link is not displayed.");

        softAssert.assertAll();
    }
}
