package com.solvd.demo;

import com.solvd.demo.pages.HomePage;
import com.solvd.demo.pages.LoginPage;
import com.solvd.demo.pages.ResetAuthenticatorPage;
import com.solvd.demo.pages.ResetPasswordPage;
import com.solvd.demo.pages.VerifyWithEmailPage;
import com.zebrunner.agent.core.annotation.TestCaseKey;
import com.zebrunner.carina.utils.R;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class PasswordResetWithValidEmailTest extends BaseTest {

    private static final String REGISTERED_EMAIL = "dmitryprimudriv+1@gmail.com";
    private static final String VERIFY_HEADING = "Verify with your email";
    private static final String VERIFY_MESSAGE_FRAGMENT = "We sent you a verification email";

    @Test(description = "Initiate password reset with valid registered email")
    @TestCaseKey("PLTV2-1488")
    public void testInitiatePasswordResetWithValidRegisteredEmail() {
        SoftAssert softAssert = new SoftAssert();

        // Step 0: Open Under Armour homepage
        HomePage homePage = new HomePage(getDriver());
        homePage.open();
        softAssert.assertTrue(homePage.isPageOpened(), "Under Armour home page is not opened");

        // Step 1: Accept cookies
        if (homePage.isAcceptCookiesButtonPresent()) {
            homePage.acceptCookies();
        }

        // Step 2: Navigate to login page directly
        R.CONFIG.put("url", "https://www.underarmour.com/en-us/login");
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.open();
        softAssert.assertTrue(loginPage.isForgotPasswordLinkPresent(),
                "'Forgot Password?' link is not visible on the Log In page");

        // Step 3: Click 'Forgot Password?' link
        ResetPasswordPage resetPasswordPage = loginPage.clickForgotPassword();
        softAssert.assertTrue(resetPasswordPage.isEmailAddressInputPresent(),
                "Email Address input field is not displayed on Reset your password page");
        softAssert.assertTrue(resetPasswordPage.isNextButtonPresent(),
                "'Next' button is not displayed on Reset your password page");

        // Step 4: Enter the registered email
        resetPasswordPage.enterEmail(REGISTERED_EMAIL);
        softAssert.assertEquals(resetPasswordPage.getEmailValue(), REGISTERED_EMAIL,
                "Email field value does not match the entered registered email");

        // Step 5: Click the 'Next' button
        ResetAuthenticatorPage resetAuthenticatorPage = resetPasswordPage.clickNext();
        softAssert.assertTrue(resetAuthenticatorPage.isSendMeAnEmailButtonPresent(),
                "'Send me an email' authenticator button is not displayed on Reset your password page");

        // Step 6: Click 'Send me an email' button
        VerifyWithEmailPage verifyWithEmailPage = resetAuthenticatorPage.clickSendMeAnEmail();
        softAssert.assertTrue(verifyWithEmailPage.isVerifyWithEmailHeadingPresent(),
                "'Verify with your email' heading is not displayed");
        softAssert.assertEquals(verifyWithEmailPage.getVerifyWithEmailHeadingText(), VERIFY_HEADING,
                "'Verify with your email' heading text does not match expected value");
        softAssert.assertTrue(verifyWithEmailPage.isVerificationConfirmationMessagePresent(),
                "Verification email confirmation message is not displayed");
        softAssert.assertTrue(
                verifyWithEmailPage.getVerificationConfirmationMessageText().contains(VERIFY_MESSAGE_FRAGMENT),
                "Verification confirmation message does not contain expected text fragment: '" +
                        VERIFY_MESSAGE_FRAGMENT + "'");

        softAssert.assertAll();
    }
}
