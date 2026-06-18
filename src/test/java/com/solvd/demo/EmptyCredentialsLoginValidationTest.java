package com.solvd.demo;

import com.solvd.demo.pages.HomePage;
import com.solvd.demo.pages.LoginPage;
import com.zebrunner.agent.core.annotation.TestCaseKey;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class EmptyCredentialsLoginValidationTest extends BaseTest {

    private static final String GLOBAL_ERROR_TEXT =
            "We found some errors. Please review the form and make corrections.";
    private static final String INLINE_ERROR_TEXT = "This field cannot be left blank.";

    @Test(description = "Login with empty email and empty password fields displays inline and global validation errors")
    @TestCaseKey("PLTV2-1628")
    public void testEmptyCredentialsLoginValidation() {
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

        // Step 1: Click 'Log In or Join' control in the header
        homePage.clickLogInOrJoin();
        softAssert.assertTrue(homePage.isLogInDropdownLinkPresent(),
                "'Log In' option is not displayed in the header dropdown");

        // Step 2: Click 'Log In' in the header dropdown
        LoginPage loginPage = homePage.clickLogInInDropdown();
        softAssert.assertTrue(loginPage.isEmailAddressInputPresent(),
                "Email Address field is not displayed on the Log In page");
        softAssert.assertTrue(loginPage.isPasswordInputPresent(),
                "Password field is not displayed on the Log In page");
        softAssert.assertTrue(loginPage.isLogInSubmitButtonPresent(),
                "'Log In' submit button is not displayed on the Log In page");

        // Step 3: Email Address field remains empty
        softAssert.assertEquals(loginPage.getEmailValue(), "",
                "Email Address field is not empty");

        // Step 4: Password field remains empty
        softAssert.assertEquals(loginPage.getPasswordValue(), "",
                "Password field is not empty");

        // Step 5: Submit the form with both fields empty.
        // The Okta form validates a required field only after it receives input and loses focus, so
        // we touch both fields (leaving them empty) to reproduce the documented behaviour where BOTH
        // fields are flagged after submission.
        loginPage.triggerEmptyFieldsValidation();
        loginPage.clickLogInSubmit();

        // Global error banner appears
        softAssert.assertTrue(loginPage.isGlobalErrorBannerPresent(),
                "Global error banner is not displayed after submitting empty form");
        softAssert.assertTrue(loginPage.getGlobalErrorBannerText().contains(GLOBAL_ERROR_TEXT),
                "Global error banner text does not contain expected message: '" + GLOBAL_ERROR_TEXT + "'");

        // Email inline error
        softAssert.assertTrue(loginPage.isEmailInlineErrorPresent(),
                "Email Address inline 'cannot be left blank' error is not displayed");
        softAssert.assertTrue(loginPage.getEmailInlineErrorText().contains(INLINE_ERROR_TEXT),
                "Email Address inline error text does not contain expected message: '" + INLINE_ERROR_TEXT + "'");

        // Password inline error
        softAssert.assertTrue(loginPage.isPasswordInlineErrorPresent(),
                "Password inline 'cannot be left blank' error is not displayed");
        softAssert.assertTrue(loginPage.getPasswordInlineErrorText().contains(INLINE_ERROR_TEXT),
                "Password inline error text does not contain expected message: '" + INLINE_ERROR_TEXT + "'");

        // Fields marked as invalid
        softAssert.assertEquals(loginPage.getEmailInvalidState(), "true",
                "Email Address field is not marked as invalid (aria-invalid=true)");
        softAssert.assertEquals(loginPage.getPasswordInvalidState(), "true",
                "Password field is not marked as invalid (aria-invalid=true)");

        // Step 6: Verify state of other form elements after failed submission
        softAssert.assertFalse(loginPage.isKeepMeLoggedInCheckboxInputChecked(),
                "'Keep me logged in' checkbox is not unchecked after failed submission");
        softAssert.assertTrue(loginPage.isForgotPasswordButtonPresent(),
                "'Forgot Password?' link is not present/interactive after failed submission");
        softAssert.assertTrue(loginPage.isCreateAccountLinkPresent(),
                "'Create Account' link is not present/interactive after failed submission");
        softAssert.assertTrue(loginPage.isLogInSubmitButtonEnabled(),
                "'Log In' button is not enabled after failed submission");
        softAssert.assertTrue(loginPage.isContinueAsGuestButtonEnabled(),
                "'Continue as a Guest' button is not enabled after failed submission");

        // User is not redirected away from the login page
        softAssert.assertTrue(getDriver().getCurrentUrl().contains("login.shop.underarmour.com"),
                "User was redirected away from the login page. Current URL: " + getDriver().getCurrentUrl());

        softAssert.assertAll();
    }
}
