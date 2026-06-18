package com.solvd.demo;

import com.solvd.demo.pages.HomePage;
import com.solvd.demo.pages.LoginPage;
import com.zebrunner.agent.core.annotation.TestCaseKey;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class IncorrectPasswordLoginErrorTest extends BaseTest {

    private static final String REGISTERED_EMAIL = "dprymudrau@gmail.com";
    private static final String INCORRECT_PASSWORD = "WrongPass123!";
    private static final String ERROR_BANNER_TEXT =
            "Incorrect password or account not found. Please check your password or register a new account.";

    @Test(description = "Verify error banner appears when logging in with valid email and incorrect password on Under Armour login page")
    @TestCaseKey("PLTV2-1629")
    public void testIncorrectPasswordLoginError() {
        SoftAssert softAssert = new SoftAssert();

        // Step 0: Open Under Armour US homepage
        HomePage homePage = new HomePage(getDriver());
        homePage.open();
        softAssert.assertTrue(homePage.isPageOpened(), "Under Armour home page is not opened");

        // Accept cookies if banner is present
        if (homePage.isAcceptCookiesButtonPresent()) {
            homePage.acceptCookies();
        }
        homePage.dismissOverlays();

        softAssert.assertTrue(homePage.isLogInOrJoinButtonPresent(),
                "'Log In or Join' control is not visible in the header");

        // Step 1: Click 'Log In or Join' button in the header
        homePage.clickLogInOrJoin();
        softAssert.assertTrue(homePage.isLogInDropdownLinkPresent(),
                "'Log In' option is not displayed in the account dropdown");

        // Step 2: Click 'Log In' link in the account dropdown
        LoginPage loginPage = homePage.clickLogInInDropdown();
        softAssert.assertTrue(loginPage.isEmailAddressInputPresent(),
                "Email Address field is not displayed on the Log In page");
        softAssert.assertTrue(loginPage.isPasswordInputPresent(),
                "Password field is not displayed on the Log In page");
        softAssert.assertTrue(loginPage.isLogInSubmitButtonPresent(),
                "'Log In' submit button is not displayed on the Log In page");

        // Step 3: Enter valid registered email
        loginPage.enterEmail(REGISTERED_EMAIL);
        softAssert.assertEquals(loginPage.getEmailValue(), REGISTERED_EMAIL,
                "Email Address field value does not match the entered registered email");

        // Step 4: Enter an incorrect password
        loginPage.enterPassword(INCORRECT_PASSWORD);
        softAssert.assertEquals(loginPage.getPasswordValue(), INCORRECT_PASSWORD,
                "Password field value does not match the entered incorrect password");

        // Step 5: Submit the form
        loginPage.clickLogInSubmit();

        // Error banner is displayed with the expected message
        softAssert.assertTrue(loginPage.isIncorrectPasswordErrorBannerPresent(),
                "Incorrect password error banner is not displayed after submitting wrong credentials");
        softAssert.assertTrue(loginPage.getIncorrectPasswordErrorBannerText().contains(ERROR_BANNER_TEXT),
                "Error banner text does not contain expected message: '" + ERROR_BANNER_TEXT + "'");

        // User remains on the Log In page (not logged in)
        softAssert.assertTrue(getDriver().getCurrentUrl().contains("login.shop.underarmour.com"),
                "User was redirected away from the Log In page. Current URL: " + getDriver().getCurrentUrl());

        // Step 6: Email Address field retains the entered email
        softAssert.assertEquals(loginPage.getEmailValue(), REGISTERED_EMAIL,
                "Email Address field did not retain the entered email after failed login attempt");

        // Error banner remains visible
        softAssert.assertTrue(loginPage.isIncorrectPasswordErrorBannerPresent(),
                "Error banner is no longer visible after the failed login attempt");

        softAssert.assertAll();
    }
}
