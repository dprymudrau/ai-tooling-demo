package com.solvd.demo;

import com.solvd.demo.pages.ForgotPasswordPage;
import com.solvd.demo.pages.HomePage;
import com.solvd.demo.pages.LoginPage;
import com.solvd.demo.pages.VerifyEmailPage;
import com.solvd.demo.utils.GmailReader;
import com.zebrunner.agent.core.annotation.TestCaseKey;
import com.zebrunner.carina.core.IAbstractTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class ForgotPasswordTest implements IAbstractTest {

    private static final String GMAIL_LOGIN = "dmitryprimudriv@gmail.com";
    private static final String GMAIL_APP_PASSWORD = "ympm vgzi sufa sxeo";
    private static final String RESET_EMAIL_TO = "dmitryprimudriv+1@gmail.com";
    private static final String RESET_EMAIL_SUBJECT_CONTAINS = "Password Reset";
    private static final int EMAIL_TIMEOUT_SECONDS = 180;

    @TestCaseKey("PLTV2-1124")
    @Test(description = "Request password reset email from Forgot Password page")
    public void testRequestPasswordResetEmailFromForgotPasswordPage() {
        SoftAssert softAssert = new SoftAssert();

        // Step 0: Open Under Armour homepage
        HomePage homePage = new HomePage(getDriver());
        homePage.open();
        softAssert.assertTrue(homePage.isPageOpened(), "Home page is not opened");

        // Step 1: Accept cookies
        softAssert.assertTrue(homePage.isAcceptCookiesButtonPresent(),
                "Accept Cookies button is not present on the home page");
        homePage.acceptCookies();

        // Step 2: Navigate to Login page
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.open();
        softAssert.assertTrue(loginPage.isPageOpened(), "Login page is not opened");
        softAssert.assertTrue(loginPage.isForgotPasswordLinkPresent(),
                "Forgot Password link is not visible on the Login page");

        // Step 3: Click 'Forgot Password?'
        ForgotPasswordPage forgotPasswordPage = loginPage.clickForgotPasswordLink();
        softAssert.assertTrue(forgotPasswordPage.isEmailInputFieldPresent(),
                "Email input field is not present on Forgot Password page");

        // Step 4: Enter the registered email
        forgotPasswordPage.typeEmail(RESET_EMAIL_TO);

        // Step 5: Click Next
        VerifyEmailPage verifyEmailPage = forgotPasswordPage.clickNextButton();
        softAssert.assertTrue(verifyEmailPage.isSendMeAnEmailButtonPresent(),
                "'Send me an email' button is not present on Verify your email page");

        // Step 6: Click 'Send me an email'
        verifyEmailPage.clickSendMeAnEmail();

        // Step 6 expected result: 'Verify with your email' screen with extra options visible
        softAssert.assertTrue(verifyEmailPage.isVerifyWithYourEmailHeadingPresent(),
                "'Verify with your email' heading is not displayed after clicking 'Send me an email'");
        softAssert.assertEquals(verifyEmailPage.getVerifyWithYourEmailHeadingText(),
                "Verify with your email",
                "'Verify with your email' heading text does not match the expected value");
        softAssert.assertTrue(verifyEmailPage.isEnterVerificationCodeInsteadLinkPresent(),
                "'Enter a verification code instead' option is not visible");
        softAssert.assertTrue(verifyEmailPage.isBackToLogInLinkPresent(),
                "'Back to Log In' link is not visible");

        // Step 7: Verify the password reset email arrives in the inbox
        String emailBody = GmailReader.fetchLatestEmailBody(
                GMAIL_LOGIN,
                GMAIL_APP_PASSWORD,
                RESET_EMAIL_TO,
                RESET_EMAIL_SUBJECT_CONTAINS,
                EMAIL_TIMEOUT_SECONDS);

        softAssert.assertNotNull(emailBody,
                "Password reset email was not received within " + EMAIL_TIMEOUT_SECONDS + " seconds");
        if (emailBody != null) {
            softAssert.assertTrue(emailBody.contains("login.shop.underarmour.com/email/verify"),
                    "Password reset email body does not contain the expected 'Reset Password' link");
        }

        softAssert.assertAll();
    }
}
