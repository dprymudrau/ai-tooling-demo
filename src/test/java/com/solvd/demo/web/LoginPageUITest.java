package com.solvd.demo.web;

import com.solvd.demo.pages.HomePage;
import com.solvd.demo.pages.LoginPage;
import com.zebrunner.agent.core.annotation.TestCaseKey;
import com.zebrunner.carina.core.IAbstractTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class LoginPageUITest implements IAbstractTest {

    @Test(description = "Verify login page UI elements are displayed correctly on load")
    @TestCaseKey("PIL-73")
    public void testLoginPageUIElements() {
        // Step 0: Open Under Armour homepage
        HomePage homePage = new HomePage(getDriver());
        homePage.open();

        // Step 1: Accept cookies banner
        homePage.acceptCookies();

        // Step 2: Navigate to Login page
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.open();

        SoftAssert softAssert = new SoftAssert();

        // Step 3: Verify Login card container is present
        softAssert.assertTrue(loginPage.getLoginCardContainer().isElementPresent(),
                "Login card container is not present");

        // Step 3 (continued): Verify UA Logo
        softAssert.assertTrue(loginPage.getUaLogo().isElementPresent(),
                "UA Logo is not present");

        // Step 3 (continued): Verify Log In heading text
        softAssert.assertTrue(loginPage.getLogInHeading().isElementPresent(),
                "Log In heading is not present");
        softAssert.assertEquals(loginPage.getLogInHeading().getText().trim(), "Log In",
                "Log In heading text is incorrect");

        // Step 4: Verify Email Address input
        softAssert.assertTrue(loginPage.getEmailAddressInput().isElementPresent(),
                "Email Address input is not present");

        // Step 4 (continued): Verify Password input
        softAssert.assertTrue(loginPage.getPasswordInput().isElementPresent(),
                "Password input is not present");

        // Step 4 (continued): Verify Show password toggle
        softAssert.assertTrue(loginPage.getShowPasswordToggleButton().isElementPresent(),
                "Show password toggle button is not present");

        // Step 5: Verify Keep me logged in checkbox
        softAssert.assertTrue(loginPage.getKeepMeLoggedInCheckbox().isElementPresent(),
                "Keep me logged in checkbox is not present");

        // Step 5 (continued): Verify checkbox is not checked by default
        softAssert.assertFalse(loginPage.getKeepMeLoggedInCheckboxInput().isChecked(),
                "Keep me logged in checkbox should be unchecked by default");

        // Step 5 (continued): Verify Forgot Password link
        softAssert.assertTrue(loginPage.getForgotPasswordLink().isElementPresent(),
                "Forgot Password link is not present");

        // Step 6: Verify Log In submit button
        softAssert.assertTrue(loginPage.getLogInSubmitButton().isElementPresent(),
                "Log In submit button is not present");

        // Step 6 (continued): Verify Continue as a Guest button
        softAssert.assertTrue(loginPage.getContinueAsGuestButton().isElementPresent(),
                "Continue as a Guest button is not present");

        // Step 7: Verify New to Under Armour text
        softAssert.assertTrue(loginPage.getNewToUnderArmourText().isElementPresent(),
                "New to Under Armour text is not present");
        softAssert.assertTrue(loginPage.getNewToUnderArmourText().getText().contains("New to Under Armour?"),
                "New to Under Armour text content is incorrect");

        // Step 7 (continued): Verify Create Account link
        softAssert.assertTrue(loginPage.getCreateAccountLink().isElementPresent(),
                "Create Account link is not present");

        // Step 7 (continued): Verify UA Rewards Terms & Conditions link
        softAssert.assertTrue(loginPage.getUaRewardsTermsLink().isElementPresent(),
                "UA Rewards' Terms & Conditions link is not present");

        // Step 7 (continued): Verify Privacy Policy link
        softAssert.assertTrue(loginPage.getPrivacyPolicyLink().isElementPresent(),
                "Privacy Policy link is not present");

        // Step 7 (continued): Verify Terms & Conditions link
        softAssert.assertTrue(loginPage.getTermsAndConditionsLink().isElementPresent(),
                "Terms & Conditions link is not present");

        softAssert.assertAll();
    }
}
