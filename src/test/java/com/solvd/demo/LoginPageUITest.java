package com.solvd.demo;

import com.solvd.demo.pages.HomePage;
import com.solvd.demo.pages.LoginPage;
import com.zebrunner.agent.core.annotation.TestCaseKey;
import com.zebrunner.carina.core.IAbstractTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class LoginPageUITest implements IAbstractTest {

    @Test(description = "Verify login page UI elements are displayed correctly on initial load")
    @TestCaseKey("PIL-209")
    public void testLoginPageUIElementsDisplayedOnInitialLoad() {
        // Step 0: Open a web browser and navigate to https://www.underarmour.com/en-us/
        HomePage homePage = new HomePage(getDriver());
        homePage.open();

        // Step 1: Accept the cookies banner by clicking the 'I ACCEPT' button if it is displayed
        homePage.acceptCookiesIfPresent();

        // Step 2: Navigate to the login page directly
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.open();

        SoftAssert softAssert = new SoftAssert();

        // Step 3: Verify the Under Armour logo and 'Log In' title are displayed at the top of the card
        softAssert.assertTrue(loginPage.getLoginCardContainer().isElementPresent(),
                "Login Card Container is not displayed");
        softAssert.assertTrue(loginPage.getUnderArmourLogo().isElementPresent(),
                "Under Armour Logo is not displayed");
        softAssert.assertEquals(loginPage.getLogInTitle().getText(), "Log In",
                "Log In title text does not match expected value");

        // Step 4: Verify Email Address and Password input fields are displayed with floating labels
        // and password masking (with eye toggle icon)
        softAssert.assertTrue(loginPage.getEmailAddressInput().isElementPresent(),
                "Email Address Input is not displayed");
        softAssert.assertEquals(loginPage.getEmailAddressLabel().getText(), "Email Address",
                "Email Address label text does not match expected value");
        softAssert.assertTrue(loginPage.getPasswordInput().isElementPresent(),
                "Password Input is not displayed");
        softAssert.assertEquals(loginPage.getPasswordLabel().getText(), "Password",
                "Password label text does not match expected value");
        softAssert.assertTrue(loginPage.getShowPasswordToggleIcon().isElementPresent(),
                "Show Password Toggle Icon is not displayed");

        // Step 5: Verify 'Keep me logged in' checkbox (unchecked) and 'Forgot Password?' link are displayed
        softAssert.assertTrue(loginPage.getKeepMeLoggedInCheckbox().isElementPresent(),
                "Keep Me Logged In Checkbox is not displayed");
        softAssert.assertFalse(loginPage.isKeepMeLoggedInCheckboxChecked(),
                "Keep Me Logged In Checkbox should be unchecked on initial load");
        softAssert.assertTrue(loginPage.getForgotPasswordLink().isElementPresent(),
                "Forgot Password Link is not displayed");

        // Step 6: Verify 'Log In' (primary) and 'Continue as a Guest' (secondary) buttons are displayed
        softAssert.assertTrue(loginPage.getLogInButton().isElementPresent(),
                "Log In Button is not displayed");
        softAssert.assertTrue(loginPage.getContinueAsGuestButton().isElementPresent(),
                "Continue as a Guest Button is not displayed");

        // Step 7: Verify the 'New to Under Armour?' text and 'Create Account' link are visible below the buttons
        softAssert.assertEquals(loginPage.getNewToUnderArmourText().getText(), "New to Under Armour?",
                "New to Under Armour? text does not match expected value");
        softAssert.assertTrue(loginPage.getCreateAccountLink().isElementPresent(),
                "Create Account Link is not displayed");

        // Step 8: Verify the legal text with hyperlinks 'UA Rewards' Terms & Conditions',
        // 'Privacy Policy', and 'Terms & Conditions' is displayed at the bottom of the card
        softAssert.assertTrue(loginPage.getUaRewardsTermsLink().isElementPresent(),
                "UA Rewards' Terms & Conditions Link is not displayed");
        softAssert.assertTrue(loginPage.getPrivacyPolicyLink().isElementPresent(),
                "Privacy Policy Link is not displayed");
        softAssert.assertTrue(loginPage.getTermsAndConditionsLink().isElementPresent(),
                "Terms & Conditions Link is not displayed");

        softAssert.assertAll();
    }
}
