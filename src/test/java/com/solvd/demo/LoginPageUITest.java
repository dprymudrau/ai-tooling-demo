package com.solvd.demo;

import com.solvd.demo.pages.HomePage;
import com.solvd.demo.pages.LoginPage;
import com.zebrunner.agent.core.annotation.TestCaseKey;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class LoginPageUITest extends BaseTest {

    @Test(description = "Verify Login page UI elements are displayed correctly")
    @TestCaseKey("PLTV2-1453")
    public void verifyLoginPageUIElementsAreDisplayedCorrectly() {
        SoftAssert softAssert = new SoftAssert();

        // Step 0: Navigate to https://www.underarmour.com/en-us/
        HomePage homePage = new HomePage(getDriver());
        homePage.open();
        softAssert.assertTrue(homePage.isPageOpened(), "Home page is not opened");

        // Step 1: Click on the 'Log In or Join' button in the site header
        // The site renders the account button with data-testid="my-account-switcher" (aria-label="Log In").
        // Check DOM presence (not visibility) because the rewards/account dropdown markup may be present-but-hidden depending on auth state.
        softAssert.assertTrue(homePage.isLogInOrJoinButtonInDom(),
                "'Log In or Join' header button is not present in DOM");

        // Step 2: Click on the 'Log In' link in the dropdown (handled inside openLoginPage)
        LoginPage loginPage = homePage.openLoginPage();

        // The 'Log In' link is present in the home page DOM (it triggers OAuth redirect to login.shop.underarmour.com)
        // After navigation the home page DOM is unloaded, so we only validate via JS-check before opening login page.
        // Successful navigation to the LoginPage proves the dropdown link worked.
        softAssert.assertTrue(loginPage.isPageOpened(), "Login page did not open after clicking the dropdown Log In link");

        // Step 3: Observe the header area - UA logo + 'Log In' title
        softAssert.assertTrue(loginPage.getUaLogo().isElementPresent(),
                "UA logo is not displayed in the header area");
        softAssert.assertTrue(loginPage.getLogInTitle().isElementPresent(),
                "'Log In' title is not displayed");
        softAssert.assertEquals(loginPage.getLogInTitle().getText().trim(), "Log In",
                "'Log In' title text does not match expected value");

        // Step 4: Email Address input field
        softAssert.assertTrue(loginPage.getEmailAddressInput().isElementPresent(),
                "Email Address input is not present");
        softAssert.assertTrue(loginPage.getEmailAddressLabel().isElementPresent(),
                "Email Address floating label is not present");
        softAssert.assertEquals(loginPage.getEmailAddressLabel().getText().trim(), "Email Address",
                "Email Address label text does not match expected value");
        softAssert.assertEquals(loginPage.getEmailAddressInput().getAttribute("value"), "",
                "Email Address input is not empty by default");

        // Step 5: Password input field
        softAssert.assertTrue(loginPage.getPasswordInput().isElementPresent(),
                "Password input is not present");
        softAssert.assertTrue(loginPage.getPasswordLabel().isElementPresent(),
                "Password floating label is not present");
        softAssert.assertEquals(loginPage.getPasswordLabel().getText().trim(), "Password",
                "Password label text does not match expected value");
        softAssert.assertTrue(loginPage.getShowPasswordEyeIcon().isElementPresent(),
                "Show password eye icon (toggle) is not present");

        // Step 6: 'Keep me logged in' checkbox + 'Forgot Password?' link
        softAssert.assertTrue(loginPage.getKeepMeLoggedInCheckboxLabel().isElementPresent(),
                "'Keep me logged in' checkbox label is not present");
        softAssert.assertTrue(loginPage.isKeepMeLoggedInCheckboxUnchecked(),
                "'Keep me logged in' checkbox is expected to be unchecked by default");
        softAssert.assertTrue(loginPage.getForgotPasswordLink().isElementPresent(),
                "'Forgot Password?' link is not present");

        // Step 7: Action buttons - 'Log In' and 'Continue as a Guest'
        softAssert.assertTrue(loginPage.getLogInSubmitButton().isElementPresent(),
                "'Log In' submit button is not present");
        softAssert.assertTrue(loginPage.getContinueAsGuestButton().isElementPresent(),
                "'Continue as a Guest' button is not present");

        // Step 8: Registration prompt and footer legal text
        softAssert.assertTrue(loginPage.getNewToUnderArmourText().isElementPresent(),
                "'New to Under Armour?' text is not displayed");
        softAssert.assertTrue(loginPage.getNewToUnderArmourText().getText().contains("New to Under Armour?"),
                "'New to Under Armour?' text does not contain expected value");
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
