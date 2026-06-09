package com.solvd.demo;

import com.solvd.demo.pages.HomePage;
import com.solvd.demo.pages.LoginPage;
import com.zebrunner.agent.core.annotation.TestCaseKey;
import com.zebrunner.carina.core.registrar.ownership.MethodOwner;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class LoginPageUITest extends BaseTest {

    @Test(description = "Verify Login page UI elements are displayed correctly")
    @TestCaseKey("PLTV2-1374")
    @MethodOwner(owner = "qagents")
    public void verifyLoginPageUIElementsAreDisplayedCorrectly() {
        SoftAssert softAssert = new SoftAssert();

        // Step 0: Open Under Armour homepage
        HomePage homePage = new HomePage(getDriver());
        homePage.open();
        softAssert.assertTrue(homePage.isPageOpened(), "Under Armour home page is not opened");

        // Step 1: Accept cookies
        homePage.acceptCookiesIfPresent();

        // Step 2: Navigate to Login page
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.open();
        softAssert.assertTrue(loginPage.isPageOpened(), "Login page is not opened");

        // Step 3: Verify Login card container is displayed
        softAssert.assertTrue(loginPage.getLoginCardContainer().isElementPresent(),
                "Login card container is not displayed");

        // Step 4: Verify UA logo is displayed at the top of the card
        softAssert.assertTrue(loginPage.getUaLogo().isElementPresent(),
                "UA Logo is not displayed at the top of the card");

        // Step 5: Verify the 'Log In' page heading is displayed and has correct text
        softAssert.assertTrue(loginPage.getLogInHeading().isElementPresent(),
                "Log In heading is not displayed");
        softAssert.assertEquals(loginPage.getLogInHeading().getText(), "Log In",
                "Log In heading text is not as expected");

        // Step 6: Verify Email Address input field is displayed
        softAssert.assertTrue(loginPage.getEmailAddressInput().isElementPresent(),
                "Email Address input field is not displayed");

        // Step 7: Verify Password input field is displayed
        softAssert.assertTrue(loginPage.getPasswordInput().isElementPresent(),
                "Password input field is not displayed");

        // Verify Show password eye icon is displayed
        softAssert.assertTrue(loginPage.getShowPasswordEyeIcon().isElementPresent(),
                "Show password eye icon is not displayed");

        // Step 8: Verify 'Keep me logged in' checkbox is displayed
        softAssert.assertTrue(loginPage.getKeepMeLoggedInCheckbox().isElementPresent(),
                "'Keep me logged in' checkbox is not displayed");

        // Verify 'Forgot Password?' link is displayed
        softAssert.assertTrue(loginPage.getForgotPasswordLink().isElementPresent(),
                "'Forgot Password?' link is not displayed");

        // Step 9: Verify primary 'Log In' button is displayed
        softAssert.assertTrue(loginPage.getLogInButton().isElementPresent(),
                "'Log In' button is not displayed");

        // Verify secondary 'Continue as a Guest' button is displayed
        softAssert.assertTrue(loginPage.getContinueAsGuestButton().isElementPresent(),
                "'Continue as a Guest' button is not displayed");

        // Step 10: Verify 'New to Under Armour?' text is displayed
        softAssert.assertTrue(loginPage.getNewToUnderArmourText().isElementPresent(),
                "'New to Under Armour?' text is not displayed");
        softAssert.assertTrue(loginPage.getNewToUnderArmourText().getText().contains("New to Under Armour?"),
                "'New to Under Armour?' text is not as expected");

        // Verify 'Create Account' link is displayed
        softAssert.assertTrue(loginPage.getCreateAccountLink().isElementPresent(),
                "'Create Account' link is not displayed");

        // Step 11: Verify legal text hyperlinks
        softAssert.assertTrue(loginPage.getUaRewardsTermsLink().isElementPresent(),
                "'UA Rewards' Terms & Conditions' link is not displayed");
        softAssert.assertTrue(loginPage.getPrivacyPolicyLink().isElementPresent(),
                "'Privacy Policy' link is not displayed");
        softAssert.assertTrue(loginPage.getTermsAndConditionsLink().isElementPresent(),
                "'Terms & Conditions' link is not displayed");

        softAssert.assertAll();
    }
}
