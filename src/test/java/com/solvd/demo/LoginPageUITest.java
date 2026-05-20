package com.solvd.demo;

import com.solvd.demo.pages.UnderArmourHomePage;
import com.solvd.demo.pages.UnderArmourLoginPage;
import com.zebrunner.agent.core.annotation.TestCaseKey;
import com.zebrunner.carina.core.IAbstractTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class LoginPageUITest implements IAbstractTest {

    @Test(description = "Verify login page UI elements are displayed correctly in initial empty state")
    @TestCaseKey("PIL-170")
    public void verifyLoginPageUIElementsInitialEmptyState() {
        // Step 0: Open Under Armour homepage
        UnderArmourHomePage homePage = new UnderArmourHomePage(getDriver());
        homePage.open();

        // Step 1: Accept cookies on consent banner
        homePage.clickAcceptCookies();

        // Step 2: Navigate to the Log In page
        UnderArmourLoginPage loginPage = new UnderArmourLoginPage(getDriver());
        loginPage.open();

        SoftAssert softAssert = new SoftAssert();

        // Step 3: Verify the header area displays the UA logo and 'Log In' heading
        softAssert.assertTrue(loginPage.getLogInHeading().isElementPresent(),
                "'Log In' heading is not displayed on the page");

        // Step 4: Verify the Email Address input field is displayed and enabled
        softAssert.assertTrue(loginPage.getEmailAddressInputField().isElementPresent(),
                "Email Address input field is not displayed");
        softAssert.assertTrue(loginPage.getEmailAddressInputField().isEnabled(),
                "Email Address input field is not enabled");

        // Step 5: Verify the Password input field and show/hide toggle
        softAssert.assertTrue(loginPage.getPasswordInputField().isElementPresent(),
                "Password input field is not displayed");
        softAssert.assertTrue(loginPage.getPasswordInputField().isEnabled(),
                "Password input field is not enabled");
        softAssert.assertTrue(loginPage.getShowPasswordToggleButton().isElementPresent(),
                "Show password toggle button is not displayed");

        // Step 6: Verify 'Keep me logged in' checkbox and 'Forgot Password?' link
        softAssert.assertTrue(loginPage.getKeepMeLoggedInCheckbox().isElementPresent(),
                "'Keep me logged in' checkbox is not displayed");
        softAssert.assertTrue(loginPage.getForgotPasswordLink().isElementPresent(),
                "'Forgot Password?' link is not displayed");

        // Step 7: Verify primary and secondary action buttons
        softAssert.assertTrue(loginPage.getLogInButton().isElementPresent(),
                "'Log In' button is not displayed");
        softAssert.assertTrue(loginPage.getLogInButton().isEnabled(),
                "'Log In' button is not enabled");
        softAssert.assertTrue(loginPage.getContinueAsGuestButton().isElementPresent(),
                "'Continue as a Guest' button is not displayed");
        softAssert.assertTrue(loginPage.getContinueAsGuestButton().isEnabled(),
                "'Continue as a Guest' button is not enabled");

        // Step 8: Verify the registration prompt and footer legal text
        softAssert.assertTrue(loginPage.getNewToUnderArmourText().isElementPresent(),
                "'New to Under Armour?' text is not displayed");
        softAssert.assertTrue(loginPage.getCreateAccountLink().isElementPresent(),
                "'Create Account' link is not displayed");
        softAssert.assertTrue(loginPage.getUaRewardsTermsAndConditionsLink().isElementPresent(),
                "'UA Rewards' Terms & Conditions' link is not displayed");
        softAssert.assertTrue(loginPage.getPrivacyPolicyLink().isElementPresent(),
                "'Privacy Policy' link is not displayed");
        softAssert.assertTrue(loginPage.getTermsAndConditionsLink().isElementPresent(),
                "'Terms & Conditions' link is not displayed");

        softAssert.assertAll();
    }
}
