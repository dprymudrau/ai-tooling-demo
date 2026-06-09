package com.solvd.demo;

import com.solvd.demo.pages.HomePage;
import com.solvd.demo.pages.LoginPage;
import com.zebrunner.agent.core.annotation.TestCaseKey;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class LoginPageUITest extends BaseTest {

    @Test(description = "Verify Login page UI elements are displayed correctly in default state")
    @TestCaseKey("PLTV2-1339")
    public void testLoginPageUIElementsDefaultState() {
        // Step 0: Open Under Armour homepage
        HomePage homePage = new HomePage(getDriver());
        homePage.open();

        // Step 1: Accept cookie consent banner
        homePage.acceptCookies();

        // Step 2: Navigate to Login page
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.open();
        loginPage.assertPageOpened();

        SoftAssert softAssert = new SoftAssert();

        // Step 3: Verify UA logo is displayed
        softAssert.assertTrue(loginPage.getUaLogo().isElementPresent(),
                "UA logo is not displayed at the top of the card");

        // Step 4: Verify Log In heading is displayed with correct text
        softAssert.assertTrue(loginPage.getLogInHeading().isElementPresent(),
                "Log In heading is not displayed");
        softAssert.assertEquals(loginPage.getLogInHeading().getText().trim(), "Log In",
                "Log In heading text does not match expected value");

        // Step 5: Verify Email Address and Password fields are displayed (with show/hide toggle)
        softAssert.assertTrue(loginPage.getEmailAddressInput().isElementPresent(),
                "Email Address input field is not displayed");
        softAssert.assertTrue(loginPage.getPasswordInput().isElementPresent(),
                "Password input field is not displayed");
        softAssert.assertTrue(loginPage.getShowPasswordToggle().isElementPresent(),
                "Show password toggle (eye icon) is not displayed");

        // Step 6: Verify Keep me logged in checkbox and Forgot Password link
        softAssert.assertTrue(loginPage.getKeepMeLoggedInCheckbox().isElementPresent(),
                "Keep me logged in checkbox is not displayed");
        softAssert.assertTrue(loginPage.getForgotPasswordLink().isElementPresent(),
                "Forgot Password? link is not displayed");

        // Step 7: Verify Action buttons (Log In and Continue as a Guest)
        softAssert.assertTrue(loginPage.getLogInButton().isElementPresent(),
                "Log In button is not displayed");
        softAssert.assertTrue(loginPage.getContinueAsGuestButton().isElementPresent(),
                "Continue as a Guest button is not displayed");

        // Step 8: Verify footer text, Create Account link and legal disclaimer links
        softAssert.assertTrue(loginPage.getNewToUnderArmourText().isElementPresent(),
                "'New to Under Armour?' text is not displayed");
        softAssert.assertTrue(loginPage.getNewToUnderArmourText().getText().contains("New to Under Armour?"),
                "'New to Under Armour?' text content does not match expected value");
        softAssert.assertTrue(loginPage.getCreateAccountLink().isElementPresent(),
                "Create Account link is not displayed");
        softAssert.assertTrue(loginPage.getUaRewardsTermsLink().isElementPresent(),
                "UA Rewards' Terms & Conditions link is not displayed");
        softAssert.assertTrue(loginPage.getPrivacyPolicyLink().isElementPresent(),
                "Privacy Policy link is not displayed");
        softAssert.assertTrue(loginPage.getTermsAndConditionsLink().isElementPresent(),
                "Terms & Conditions link is not displayed");

        softAssert.assertAll();
    }
}
