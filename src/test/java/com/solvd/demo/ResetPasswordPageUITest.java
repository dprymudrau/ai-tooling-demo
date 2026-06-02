package com.solvd.demo;

import com.solvd.demo.pages.HomePage;
import com.solvd.demo.pages.LoginPage;
import com.solvd.demo.pages.ResetPasswordEmailPage;
import com.solvd.demo.pages.ResetPasswordPage;
import com.solvd.demo.pages.VerifyWithEmailPage;
import com.solvd.demo.utils.MailReader;
import com.zebrunner.agent.core.annotation.TestCaseKey;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.File;

public class ResetPasswordPageUITest extends BaseTest {

    private static final String RESET_EMAIL = "dmitryprimudriv+1@gmail.com";
    private static final String INBOX_EMAIL = "dmitryprimudriv@gmail.com";
    private static final String EMAIL_SUBJECT_HINT = "Password Reset Request";
    private static final int EMAIL_TIMEOUT_SECONDS = 180;
    private static final String LINK_FILE_PATH = "/tmp/ua_reset_link.txt";

    @Test(description = "Verify reset password page UI elements and layout")
    @TestCaseKey("PLTV2-1275")
    public void verifyResetPasswordPageUIElementsAndLayout() {
        // Clean up any stale reset link file before starting.
        new File(LINK_FILE_PATH).delete();

        // Step 0: Open Under Armour home page
        HomePage homePage = new HomePage(getDriver());
        homePage.open();
        Assert.assertTrue(homePage.isHomePageLoaded(),
                "Under Armour homepage failed to load");

        // Step 1: Click account icon -> Log In page
        LoginPage loginPage = homePage.clickAccountIcon();
        Assert.assertTrue(loginPage.isForgotPasswordLinkPresent(),
                "Log In page did not open or 'Forgot Password?' link is missing");

        // Step 2: Click 'Forgot Password?' -> Reset Password (email) page
        ResetPasswordEmailPage resetEmailPage = loginPage.clickForgotPassword();
        Assert.assertTrue(resetEmailPage.isEmailInputPresent(),
                "Reset Password (email) page did not open - email input not present");
        Assert.assertTrue(resetEmailPage.isNextButtonPresent(),
                "Reset Password (email) page Next button is missing");

        // Step 3: Enter email
        resetEmailPage.typeEmail(RESET_EMAIL);

        // Step 4: Click 'Next' button
        VerifyWithEmailPage verifyWithEmailPage = resetEmailPage.clickNext();
        Assert.assertTrue(verifyWithEmailPage.isSendMeAnEmailButtonPresent(),
                "'Verify your email' page did not display 'Send me an email' button");

        // Step 5: Click 'Send me an email' so the reset email is bound to the
        // current selenium session (otherwise the magic link opens the
        // "Your verification code" intermediate page instead of the reset form).
        verifyWithEmailPage.clickSendMeAnEmail();

        // Step 6: Read the reset link. MailReader resolves it in this order:
        //   1. RESET_PASSWORD_LINK env/sysprop
        //   2. /tmp/ua_reset_link.txt (written by an external email watcher)
        //   3. IMAP via GMAIL_USER/GMAIL_APP_PASSWORD
        String resetLink = MailReader.fetchResetPasswordLink(
                INBOX_EMAIL, EMAIL_SUBJECT_HINT, EMAIL_TIMEOUT_SECONDS);
        Assert.assertNotNull(resetLink, "Reset password link was not found in the inbox");
        Assert.assertTrue(resetLink.startsWith("https://login.shop.underarmour.com/email/verify/"),
                "Reset password link has unexpected format: " + resetLink);

        // Step 7: Open the reset password URL in the same browser
        getDriver().get(resetLink);

        // Step 8: Verify all UI elements on the Reset Password page
        ResetPasswordPage resetPasswordPage = new ResetPasswordPage(getDriver());

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(resetPasswordPage.isLogoPresent(),
                "Under Armour logo is not present on the Reset Password page");
        softAssert.assertTrue(resetPasswordPage.isResetHeadingPresent(),
                "'Reset your password' heading is not present");
        // TODO: confirm exact heading text once Okta widget version is locked.
        softAssert.assertEquals(
                resetPasswordPage.getResetHeadingTextSafe().toLowerCase().trim(),
                "reset your password",
                "'Reset your password' heading text mismatch");
        softAssert.assertTrue(resetPasswordPage.isNewPasswordFieldPresent(),
                "'New password' field is not present");
        softAssert.assertTrue(resetPasswordPage.isReEnterPasswordFieldPresent(),
                "'Re-enter password' field is not present");
        softAssert.assertTrue(resetPasswordPage.isRequirementsListPresent(),
                "Password requirements list is not present");
        softAssert.assertTrue(resetPasswordPage.isResetPasswordButtonPresent(),
                "'Reset Password' button is not present");
        softAssert.assertTrue(resetPasswordPage.isBackToLogInLinkPresent(),
                "'Back to Log In' link is not present");
        softAssert.assertAll();
    }
}
