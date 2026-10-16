package com.solvd.demo;

import com.solvd.demo.pages.HomePage;
import com.solvd.demo.pages.LoginPage;
import com.solvd.demo.pages.ResetPasswordPage;
import com.solvd.demo.pages.VerifyEmailConfirmationPage;
import com.solvd.demo.pages.VerifyEmailPage;
import com.zebrunner.agent.core.annotation.TestCaseKey;
import com.zebrunner.carina.core.IAbstractTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class PasswordResetTest implements IAbstractTest {

    private static final String REGISTERED_EMAIL = "dmitryprimudriv+1@gmail.com";
    private static final String EXPECTED_HEADING = "Verify with your email";
    private static final String EXPECTED_CONFIRMATION_MESSAGE =
            "We sent you a verification email. Click the verification link in your email to continue or enter the code below.";

    @Test(description = "Request password reset with valid registered email")
    @TestCaseKey("PLTV2-1107")
    public void testRequestPasswordResetWithValidRegisteredEmail() {
        SoftAssert softAssert = new SoftAssert();

        // Step 0: Open Under Armour homepage
        HomePage homePage = new HomePage(getDriver());
        homePage.open();
        softAssert.assertTrue(homePage.isPageOpened(), "Under Armour Home Page is not opened");

        // Step 1: Accept cookies
        if (homePage.isAcceptCookiesButtonPresent()) {
            homePage.acceptCookies();
        }
        // Dismiss any promotional email-signup overlays that intercept header clicks
        homePage.dismissPromoOverlays();

        // Step 2: Click on the 'Log In or Join' icon in the header
        softAssert.assertTrue(homePage.isLoginOrJoinButtonPresent(),
                "'Log In or Join' icon is not displayed in the header");
        homePage.clickLoginOrJoinIcon();

        // Step 3: Click on the 'Log In' button in the account dropdown
        softAssert.assertTrue(homePage.isLogInDropdownButtonPresent(),
                "'Log In' button is not displayed in the account dropdown");
        LoginPage loginPage = homePage.clickLogInFromDropdown();

        // Step 4: Click on the 'Forgot Password?' link
        Assert.assertTrue(loginPage.isForgotPasswordLinkPresent(),
                "'Forgot Password?' link is not displayed on the Log In page");
        ResetPasswordPage resetPasswordPage = loginPage.clickForgotPassword();

        // Step 5: Enter the registered email
        Assert.assertTrue(resetPasswordPage.isEmailInputPresent(),
                "Email Address input field is not displayed on the Reset Password page");
        resetPasswordPage.typeEmail(REGISTERED_EMAIL);

        // Step 6: Click the 'Next' button
        Assert.assertTrue(resetPasswordPage.isNextButtonPresent(),
                "'Next' button is not displayed on the Reset Password page");
        VerifyEmailPage verifyEmailPage = resetPasswordPage.clickNext();

        // Step 7: Click the 'Send me an email' button
        Assert.assertTrue(verifyEmailPage.isSendMeAnEmailButtonPresent(),
                "'Send me an email' button is not displayed on the Verify Email page");
        VerifyEmailConfirmationPage verifyEmailConfirmationPage = verifyEmailPage.clickSendMeAnEmail();

        // Step 8 & 9: Hard-assert confirmation page is displayed with expected texts
        Assert.assertTrue(verifyEmailConfirmationPage.isVerifyWithYourEmailHeadingPresent(),
                "'Verify with your email' heading is not displayed on the confirmation page");
        Assert.assertEquals(
                verifyEmailConfirmationPage.getVerifyWithYourEmailHeadingText().trim(),
                EXPECTED_HEADING,
                "'Verify with your email' heading text does not match");

        Assert.assertTrue(verifyEmailConfirmationPage.isVerificationEmailConfirmationMessagePresent(),
                "Verification email confirmation message is not displayed");
        Assert.assertEquals(
                verifyEmailConfirmationPage.getVerificationEmailConfirmationMessageText().trim(),
                EXPECTED_CONFIRMATION_MESSAGE,
                "Verification email confirmation message text does not match");

        softAssert.assertAll();
    }
}
