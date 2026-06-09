package com.solvd.demo;

import com.solvd.demo.pages.LoginPage;
import com.solvd.demo.pages.UnderArmourHomePage;
import com.zebrunner.agent.core.annotation.TestCaseKey;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class LoginPageUITest extends BaseTest {

    @Test(description = "Verify Log In page UI elements layout and styling")
    @TestCaseKey("PLTV2-1392")
    public void verifyLogInPageUIElementsLayoutAndStyling() {
        // Step 0: Open Under Armour homepage
        UnderArmourHomePage homePage = new UnderArmourHomePage(getDriver());
        homePage.open();
        homePage.assertPageOpened();

        // Step 1: Accept cookies if banner is displayed
        homePage.acceptCookiesIfDisplayed();

        // Step 2: Navigate to the Log In page
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.open();
        loginPage.assertPageOpened();

        SoftAssert softAssert = new SoftAssert();

        // Step 3: Verify UA logo and Log In heading
        softAssert.assertTrue(loginPage.getUaLogo().isElementPresent(),
                "UA Logo is not displayed on the Log In page");
        softAssert.assertTrue(loginPage.getLogInHeading().isElementPresent(),
                "'Log In' heading is not displayed on the Log In page");
        softAssert.assertEquals(loginPage.getLogInHeading().getText().trim(), "Log In",
                "'Log In' heading text does not match expected value");

        // Step 4: Verify Email Address input field
        softAssert.assertTrue(loginPage.getEmailAddressInput().isElementPresent(),
                "Email Address input field is not displayed");
        softAssert.assertEquals(loginPage.getEmailAddressInput().getAttribute("value"), "",
                "Email Address input field is not empty");

        // Step 5: Verify Password input field and Show password toggle
        softAssert.assertTrue(loginPage.getPasswordInput().isElementPresent(),
                "Password input field is not displayed");
        softAssert.assertEquals(loginPage.getPasswordInput().getAttribute("value"), "",
                "Password input field is not empty");
        softAssert.assertTrue(loginPage.getShowPasswordToggle().isElementPresent(),
                "Show password toggle (eye icon) is not displayed");

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

        // Step 8: Verify footer area and legal text
        softAssert.assertTrue(loginPage.getNewToUnderArmourText().isElementPresent(),
                "'New to Under Armour?' text is not displayed");
        softAssert.assertTrue(
                loginPage.getNewToUnderArmourText().getText().contains("New to Under Armour?"),
                "'New to Under Armour?' text does not match expected value");
        softAssert.assertTrue(loginPage.getCreateAccountLink().isElementPresent(),
                "'Create Account' link is not displayed");
        softAssert.assertTrue(loginPage.getUaRewardsTermsLink().isElementPresent(),
                "'UA Rewards' Terms & Conditions' link is not displayed");
        softAssert.assertTrue(loginPage.getPrivacyPolicyLink().isElementPresent(),
                "'Privacy Policy' link is not displayed");
        softAssert.assertTrue(loginPage.getTermsConditionsLink().isElementPresent(),
                "'Terms & Conditions' link is not displayed");

        softAssert.assertAll();
    }
}
