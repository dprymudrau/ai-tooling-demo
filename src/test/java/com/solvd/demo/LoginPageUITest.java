package com.solvd.demo;

import com.solvd.demo.pages.HomePage;
import com.solvd.demo.pages.LoginPage;
import com.zebrunner.agent.core.annotation.TestCaseKey;
import com.zebrunner.carina.core.IAbstractTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class LoginPageUITest implements IAbstractTest {

    @Test(description = "Verify login page UI elements are displayed correctly")
    @TestCaseKey("PIL-110")
    public void testVerifyLoginPageUIElementsAreDisplayedCorrectly() {
        SoftAssert softAssert = new SoftAssert();

        // Step 0: Open Under Armour homepage
        HomePage homePage = new HomePage(getDriver());
        homePage.open();
        softAssert.assertTrue(homePage.isPageOpened(), "Under Armour Home page is not opened");

        // Step 1: Click 'Log In or Join' button in the top header
        softAssert.assertTrue(homePage.getLogInOrJoinButton().isElementPresent(),
                "'Log In or Join' button is not displayed in the header");
        homePage.clickLogInOrJoinButton();

        // Step 2: Click the 'Log In' option in the dropdown menu
        softAssert.assertTrue(homePage.getLogInDropdownOption().isElementPresent(),
                "'Log In' option is not displayed in the dropdown menu");
        LoginPage loginPage = homePage.clickLogInOption();

        // Step 3: Inspect login card container
        softAssert.assertTrue(loginPage.getLoginCardContainer().isElementPresent(),
                "Login card container is not displayed");

        // Step 3 (UA logo)
        softAssert.assertTrue(loginPage.getUaLogo().isElementPresent(),
                "UA logo is not displayed at the top of the login card");

        // Step 3 (Log In heading)
        softAssert.assertEquals(loginPage.getLogInHeading().getText().trim(), "Log In",
                "'Log In' heading text mismatch");

        // Step 4: Email Address input + label
        softAssert.assertTrue(loginPage.getEmailAddressInput().isElementPresent(),
                "Email Address input field is not displayed");
        softAssert.assertEquals(loginPage.getEmailAddressFloatingLabel().getText().trim(), "Email Address",
                "Email Address floating label text mismatch");

        // Step 4: Password input + label + eye icon
        softAssert.assertTrue(loginPage.getPasswordInput().isElementPresent(),
                "Password input field is not displayed");
        softAssert.assertEquals(loginPage.getPasswordFloatingLabel().getText().trim(), "Password",
                "Password floating label text mismatch");
        softAssert.assertTrue(loginPage.getShowHidePasswordEyeIcon().isElementPresent(),
                "Show/Hide password eye icon is not displayed");

        // Step 5: Keep me logged in checkbox and Forgot Password link
        softAssert.assertTrue(loginPage.getKeepMeLoggedInCheckbox().isElementPresent(),
                "'Keep me logged in' checkbox is not displayed");
        softAssert.assertFalse(loginPage.getKeepMeLoggedInCheckboxInput().getElement().isSelected(),
                "'Keep me logged in' checkbox should be unchecked by default");
        softAssert.assertTrue(loginPage.getForgotPasswordLink().isElementPresent(),
                "'Forgot Password?' link is not displayed");

        // Step 6: Primary and secondary action buttons
        softAssert.assertTrue(loginPage.getLogInSubmitButton().isElementPresent(),
                "'Log In' submit button is not displayed");
        softAssert.assertTrue(loginPage.getContinueAsGuestButton().isElementPresent(),
                "'Continue as a Guest' button is not displayed");

        // Step 7: Registration prompt + legal footer text
        softAssert.assertTrue(loginPage.getNewToUnderArmourText().getText().contains("New to Under Armour?"),
                "'New to Under Armour?' text is not displayed");
        softAssert.assertTrue(loginPage.getCreateAccountLink().isElementPresent(),
                "'Create Account' link is not displayed");
        softAssert.assertTrue(loginPage.getUaRewardsTermsConditionsLink().isElementPresent(),
                "'UA Rewards' Terms & Conditions' link is not displayed in the footer");
        softAssert.assertTrue(loginPage.getPrivacyPolicyLink().isElementPresent(),
                "'Privacy Policy' link is not displayed in the footer");
        softAssert.assertTrue(loginPage.getTermsConditionsLink().isElementPresent(),
                "'Terms & Conditions' link is not displayed in the footer");

        softAssert.assertAll();
    }
}
