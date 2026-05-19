package com.solvd.demo;

import com.solvd.demo.pages.HomePage;
import com.solvd.demo.pages.LoginPage;
import com.zebrunner.agent.core.annotation.TestCaseKey;
import com.zebrunner.carina.core.IAbstractTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class LoginPageUITest implements IAbstractTest {

    @Test(description = "Verify Login page UI elements and default state display")
    @TestCaseKey("PIL-59")
    public void verifyLoginPageUIElementsAndDefaultStateDisplay() {
        // Step 0: Open Under Armour home page
        HomePage homePage = new HomePage(getDriver());
        homePage.open();

        // Step 1: Navigate to Login Page
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.open();

        SoftAssert softAssert = new SoftAssert();

        // Step 2: Verify UA Logo and 'Log In' heading
        softAssert.assertTrue(loginPage.getUaLogo().isElementPresent(),
                "UA Logo is not displayed on the login card header");
        softAssert.assertTrue(loginPage.getLogInHeading().isElementPresent(),
                "Log In heading is not displayed on the login card");
        softAssert.assertEquals(loginPage.getLogInHeading().getText().trim(), "Log In",
                "Log In heading text is incorrect");

        // Step 3: Verify Email and Password input fields and password show/hide icon
        softAssert.assertTrue(loginPage.getEmailAddressInput().isElementPresent(),
                "Email Address input is not displayed");
        softAssert.assertTrue(loginPage.getPasswordInput().isElementPresent(),
                "Password input is not displayed");
        softAssert.assertTrue(loginPage.getShowPasswordEyeIcon().isElementPresent(),
                "Show Password (eye) icon is not displayed");

        // Step 4: Verify 'Keep me logged in' checkbox and 'Forgot Password?' link
        softAssert.assertTrue(loginPage.getKeepMeLoggedInCheckbox().isElementPresent(),
                "'Keep me logged in' checkbox is not displayed");
        softAssert.assertTrue(loginPage.getForgotPasswordLink().isElementPresent(),
                "'Forgot Password?' link is not displayed");

        // Step 5: Verify action buttons
        softAssert.assertTrue(loginPage.getLogInButton().isElementPresent(),
                "'Log In' primary button is not displayed");
        softAssert.assertTrue(loginPage.getContinueAsGuestButton().isElementPresent(),
                "'Continue as a Guest' secondary button is not displayed");

        // Step 6: Verify footer area
        softAssert.assertTrue(loginPage.getNewToUnderArmourText().isElementPresent(),
                "'New to Under Armour?' text is not displayed");
        softAssert.assertTrue(loginPage.getNewToUnderArmourText().getText().contains("New to Under Armour?"),
                "'New to Under Armour?' text content is incorrect");
        softAssert.assertTrue(loginPage.getCreateAccountLink().isElementPresent(),
                "'Create Account' link is not displayed");
        softAssert.assertTrue(loginPage.getUaRewardsTermsLink().isElementPresent(),
                "'UA Rewards' Terms & Conditions' link is not displayed");
        softAssert.assertTrue(loginPage.getPrivacyPolicyLink().isElementPresent(),
                "'Privacy Policy' link is not displayed");
        softAssert.assertTrue(loginPage.getTermsAndConditionsLink().isElementPresent(),
                "'Terms & Conditions' link is not displayed");

        softAssert.assertAll();
    }
}
