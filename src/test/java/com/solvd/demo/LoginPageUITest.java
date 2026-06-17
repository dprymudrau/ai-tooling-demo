package com.solvd.demo;

import com.solvd.demo.pages.LoginPage;
import com.solvd.demo.pages.UnderArmourHomePage;
import com.zebrunner.agent.core.annotation.TestCaseKey;
import com.zebrunner.carina.core.IAbstractTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class LoginPageUITest implements IAbstractTest {

    @TestCaseKey("PLTV2-1097")
    @Test(description = "Verify login page UI elements are displayed correctly on load")
    public void testLoginPageUIElementsDisplayedCorrectly() {
        // Step 0: Open the Under Armour homepage
        UnderArmourHomePage homePage = new UnderArmourHomePage(getDriver());
        homePage.open();

        // Under Armour may redirect to /en-us/change-location/ based on the
        // selenium node IP. Recover by selecting United States so we land on
        // the actual home page before proceeding.
        homePage.handleChangeLocationIfNeeded();

        // Step 1: Accept cookies banner
        homePage.acceptCookies();

        // Step 2: Click 'Log In or Join' button in the header
        homePage.clickLogInOrJoin();

        // Step 3: Click 'Log In' link in dropdown to open Login Page
        LoginPage loginPage = homePage.openLoginPage();

        SoftAssert softAssert = new SoftAssert();

        // Step 4: Verify Under Armour Logo is displayed
        softAssert.assertTrue(loginPage.getUnderArmourLogo().isElementPresent(30),
                "Under Armour logo is not displayed at the top of the Login card");

        // Step 5: Verify 'Log In' heading is displayed with correct text
        softAssert.assertTrue(loginPage.getLogInHeading().isElementPresent(15),
                "'Log In' heading is not displayed");
        softAssert.assertEquals(loginPage.getLogInHeading().getText().trim(), "Log In",
                "'Log In' heading text does not match expected value");

        // Step 6: Verify Email Address and Password fields
        softAssert.assertTrue(loginPage.getEmailAddressField().isElementPresent(15),
                "Email Address field is not displayed");
        softAssert.assertTrue(loginPage.getPasswordField().isElementPresent(15),
                "Password field is not displayed");
        softAssert.assertTrue(loginPage.getShowPasswordEyeIcon().isElementPresent(15),
                "Show password eye icon is not displayed inside Password field");

        // Step 7: Verify 'Keep me logged in' checkbox and 'Forgot Password?' link
        softAssert.assertTrue(loginPage.getKeepMeLoggedInCheckbox().isElementPresent(15),
                "'Keep me logged in' checkbox is not displayed");
        softAssert.assertTrue(loginPage.getForgotPasswordLink().isElementPresent(15),
                "'Forgot Password?' link is not displayed");

        // Step 8: Verify primary 'Log In' submit button and secondary 'Continue as a Guest' button
        softAssert.assertTrue(loginPage.getLogInSubmitButton().isElementPresent(15),
                "Primary 'Log In' submit button is not displayed");
        softAssert.assertTrue(loginPage.getContinueAsGuestButton().isElementPresent(15),
                "Secondary 'Continue as a Guest' button is not displayed");

        // Step 9: Verify footer section - 'New to Under Armour?' text and links
        softAssert.assertTrue(loginPage.getNewToUnderArmourText().isElementPresent(15),
                "'New to Under Armour?' text is not displayed in footer section");
        softAssert.assertTrue(
                loginPage.getNewToUnderArmourText().getText().contains("New to Under Armour?"),
                "'New to Under Armour?' text content does not match expected value");
        softAssert.assertTrue(loginPage.getCreateAccountLink().isElementPresent(15),
                "'Create Account' link is not displayed in footer");
        softAssert.assertTrue(loginPage.getUaRewardsTermsLink().isElementPresent(15),
                "'UA Rewards' Terms & Conditions' link is not displayed in footer");
        softAssert.assertTrue(loginPage.getPrivacyPolicyLink().isElementPresent(15),
                "'Privacy Policy' link is not displayed in footer");
        softAssert.assertTrue(loginPage.getTermsAndConditionsLink().isElementPresent(15),
                "'Terms & Conditions' link is not displayed in footer");

        softAssert.assertAll();
    }
}
