package com.solvd.demo;

import com.solvd.demo.pages.HomePage;
import com.solvd.demo.pages.LoginPage;
import com.solvd.demo.pages.ResetPasswordPage;
import com.solvd.demo.pages.VerifyWithYourEmailPage;
import com.solvd.demo.pages.VerifyYourEmailPage;
import com.zebrunner.agent.core.annotation.TestCaseKey;
import com.zebrunner.carina.core.IAbstractTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class ForgotPasswordTest implements IAbstractTest {

    private static final String REGISTERED_EMAIL = "dmitryprimudriv+1@gmail.com";
    private static final String EXPECTED_HEADER_TEXT = "Verify with your email";
    private static final String EXPECTED_CONFIRMATION_MESSAGE =
            "We sent you a verification email. Click the verification link in your email to continue or enter the code below.";

    @Test(description = "Request password reset with valid registered email")
    @TestCaseKey("PIL-215")
    public void testRequestPasswordResetWithValidRegisteredEmail() {
        SoftAssert softAssert = new SoftAssert();

        // Step 0: Open the Under Armour homepage
        HomePage homePage = new HomePage(getDriver());
        homePage.open();
        softAssert.assertTrue(homePage.isPageOpened(), "Under Armour home page is not opened");

        // Step 1: Accept cookies
        softAssert.assertTrue(homePage.isAcceptCookiesButtonPresent(),
                "Accept Cookies button is not present on the home page");
        homePage.acceptCookies();

        // Step 2: Navigate to the login page
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.open();
        softAssert.assertTrue(loginPage.isPageOpened(), "Login page is not opened");
        softAssert.assertTrue(loginPage.isForgotPasswordLinkPresent(),
                "'Forgot Password?' link is not present on the login page");

        // Step 3: Click the 'Forgot Password?' link
        ResetPasswordPage resetPasswordPage = loginPage.clickForgotPasswordLink();
        softAssert.assertTrue(resetPasswordPage.isEmailInputPresent(),
                "Email Address input field is not present on the Reset Password page");
        softAssert.assertTrue(resetPasswordPage.isNextButtonPresent(),
                "'Next' button is not present on the Reset Password page");

        // Step 4: Enter the registered email
        resetPasswordPage.typeEmail(REGISTERED_EMAIL);
        softAssert.assertEquals(resetPasswordPage.getEmailFieldValue(), REGISTERED_EMAIL,
                "Email field value does not match the entered registered email");

        // Step 5: Click the 'Next' button
        VerifyYourEmailPage verifyYourEmailPage = resetPasswordPage.clickNext();
        softAssert.assertTrue(verifyYourEmailPage.isSendMeAnEmailButtonPresent(),
                "'Send me an email' button is not present on the Verify Your Email page");

        // Step 6: Click the 'Send me an email' button
        VerifyWithYourEmailPage verifyWithYourEmailPage = verifyYourEmailPage.clickSendMeAnEmail();

        // Step 7: Validate verification confirmation header
        softAssert.assertTrue(verifyWithYourEmailPage.isVerificationHeaderPresent(),
                "'Verify with your email' header is not present on the confirmation page");
        softAssert.assertEquals(verifyWithYourEmailPage.getVerificationHeaderText(), EXPECTED_HEADER_TEXT,
                "Verification header text does not match expected value");

        // Step 8: Validate verification confirmation message
        softAssert.assertTrue(verifyWithYourEmailPage.isVerificationMessagePresent(),
                "Verification confirmation message is not present on the confirmation page");
        softAssert.assertEquals(verifyWithYourEmailPage.getVerificationMessageText(), EXPECTED_CONFIRMATION_MESSAGE,
                "Verification confirmation message text does not match expected");

        // Additional expected elements per manual test case
        softAssert.assertTrue(verifyWithYourEmailPage.isEnterVerificationCodeLinkPresent(),
                "'Enter a verification code instead' option is not present");
        softAssert.assertTrue(verifyWithYourEmailPage.isBackToLogInLinkPresent(),
                "'Back to Log In' link is not present");

        softAssert.assertAll();
    }
}
