package com.solvd.demo;

import com.solvd.demo.pages.HomePage;
import com.solvd.demo.pages.LoginCallbackPage;
import com.solvd.demo.pages.LoginPage;
import com.solvd.demo.pages.ResetFactorSelectionPage;
import com.solvd.demo.pages.ResetPasswordFormPage;
import com.solvd.demo.pages.ResetPasswordPage;
import com.solvd.demo.pages.VerifyEmailPage;
import com.solvd.demo.utils.MailReader;
import com.zebrunner.agent.core.annotation.TestCaseKey;
import com.zebrunner.carina.core.IAbstractTest;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.Duration;

public class ResetPasswordValidTest implements IAbstractTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResetPasswordValidTest.class);

    private static final String RESET_EMAIL = "dmitryprimudriv+1@gmail.com";
    private static final String INBOX_USER = "dmitryprimudriv@gmail.com";
    private static final String INBOX_APP_PASSWORD = "ympm vgzi sufa sxeo";
    private static final String EMAIL_SUBJECT_CONTAINS = "Password Reset Request for Your UA.com Account";

    /**
     * Use a unique strong password every run. Okta rejects passwords that
     * match the user's password history, so reusing the same string would
     * silently fail. The salt is the current epoch second mixed in with
     * fixed characters that satisfy all complexity requirements
     * (8+ chars, lowercase, uppercase, digit, special).
     */
    private static String newPassword() {
        return "NewStrongPwd" + (System.currentTimeMillis() % 100_000L) + "!A";
    }

    @Test(description = "Reset password with valid new password meeting all requirements")
    @TestCaseKey("PLTV2-1178")
    public void testResetPasswordWithValidNewPassword() {
        SoftAssert softAssert = new SoftAssert();

        // Under Armour pages load very heavy 3rd-party assets. Cap the WebDriver
        // page load timeout so driver.get() returns instead of blocking forever
        // when those assets are slow.
        try {
            getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        } catch (Exception ignored) {
            // Not fatal; continue with default timeout.
        }

        // Step 0: Open Under Armour home page
        LOGGER.info("Step 0: Open Under Armour home page");
        HomePage homePage = new HomePage(getDriver());
        homePage.navigate();
        softAssert.assertTrue(homePage.isHomeUrlOpened(),
                "Under Armour home page is not opened. Current URL: " + getDriver().getCurrentUrl());

        // Step 1: Navigate to Login page
        LOGGER.info("Step 1: Navigate to Login page");
        LoginPage loginPage = homePage.openLoginPage();
        softAssert.assertTrue(loginPage.isEmailFieldPresent(),
                "Email field is not visible on Login page");
        softAssert.assertTrue(loginPage.isForgotPasswordLinkPresent(),
                "'Forgot Password?' link is not visible on Login page");

        // Step 2: Click 'Forgot Password?' link
        LOGGER.info("Step 2: Click 'Forgot Password?' link");
        ResetPasswordPage resetPasswordPage = loginPage.clickForgotPassword();
        softAssert.assertTrue(resetPasswordPage.isEmailFieldPresent(),
                "Email Address input is not visible on Reset Password page");
        softAssert.assertTrue(resetPasswordPage.isNextButtonPresent(),
                "'Next' button is not visible on Reset Password page");

        // Step 3: Enter email
        LOGGER.info("Step 3: Enter email '{}'", RESET_EMAIL);
        resetPasswordPage.typeEmail(RESET_EMAIL);

        // Step 4: Click Next
        LOGGER.info("Step 4: Click 'Next' button");
        ResetFactorSelectionPage factorPage = resetPasswordPage.clickNext();
        softAssert.assertTrue(factorPage.isSendMeAnEmailButtonPresent(),
                "'Send me an email' option is not visible on factor selection page");

        // Step 5: Click 'Send me an email'
        LOGGER.info("Step 5: Click 'Send me an email' button");
        long resetRequestedAt = System.currentTimeMillis();
        VerifyEmailPage verifyEmailPage = factorPage.clickSendMeAnEmail();
        softAssert.assertTrue(verifyEmailPage.isHeaderPresent(),
                "'Verify with your email' confirmation page is not displayed");

        // Step 6: Fetch the password reset email and extract the reset link.
        // Use the resetRequestedAt timestamp so we don't pick up stale emails
        // from previous test runs.
        LOGGER.info("Step 6: Fetch password reset email from inbox");
        String emailBody = MailReader.fetchEmailBody(
                INBOX_USER,
                INBOX_APP_PASSWORD,
                RESET_EMAIL,
                EMAIL_SUBJECT_CONTAINS,
                120,
                resetRequestedAt);
        Assert.assertNotNull(emailBody, "Reset password email body is null");
        Assert.assertFalse(emailBody.isEmpty(), "Reset password email body is empty");

        String resetLink = MailReader.extractResetLink(emailBody);
        LOGGER.info("Extracted reset link: {}", resetLink);
        Assert.assertNotNull(resetLink, "Reset link not found in email");
        Assert.assertTrue(resetLink.contains("underarmour.com"),
                "Reset link does not appear to be an underarmour.com URL: " + resetLink);

        // Step 7: Open the reset link in the browser
        LOGGER.info("Step 7: Open reset link in browser");
        ResetPasswordFormPage formPage = verifyEmailPage.openResetLink(resetLink);
        boolean newPwdPresent = formPage.isNewPasswordFieldPresent();
        boolean confirmPwdPresent = formPage.isReEnterPasswordFieldPresent();
        if (!newPwdPresent || !confirmPwdPresent) {
            LOGGER.error("Reset form not detected. URL: {} | Title: {}",
                    getDriver().getCurrentUrl(), getDriver().getTitle());
            String src = getDriver().getPageSource();
            LOGGER.error("Page source first 2000 chars: {}",
                    src == null ? "<null>" : src.substring(0, Math.min(2000, src.length())));
        }
        softAssert.assertTrue(newPwdPresent,
                "'New password' field is not visible on reset form");
        softAssert.assertTrue(confirmPwdPresent,
                "'Re-enter password' field is not visible on reset form");

        String newPwd = newPassword();
        LOGGER.info("Generated new password length={}", newPwd.length());

        // Step 8: Enter new password
        LOGGER.info("Step 8: Enter new password");
        formPage.typeNewPassword(newPwd);

        // Step 9: Re-enter new password
        LOGGER.info("Step 9: Re-enter new password");
        formPage.typeReEnterPassword(newPwd);

        // Step 10: Click Reset Password
        LOGGER.info("Step 10: Click 'Reset Password' button");
        LoginCallbackPage callbackPage = formPage.clickResetPassword();

        // Verify auto-login message - wait for the callback URL / message.
        // Okta's success flow redirects to underarmour.com/en-us/login/callback
        // where the OAuth code is exchanged.
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(60));
        boolean success;
        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.urlContains("/login/callback"),
                    ExpectedConditions.urlContains("underarmour.com/en-us/"),
                    ExpectedConditions.presenceOfElementLocated(
                            callbackPage.getLoggingYouInTextElement().getBy())));
            success = true;
        } catch (Exception e) {
            success = false;
        }

        if (!success) {
            // Dump diagnostics so we can see why the form did not submit.
            LOGGER.error("Callback not reached. URL: {} | Title: {}",
                    getDriver().getCurrentUrl(), getDriver().getTitle());
            String src = getDriver().getPageSource();
            if (src != null) {
                String lower = src.toLowerCase();
                int idx = lower.indexOf("error");
                if (idx >= 0) {
                    LOGGER.error("Page source error context: {}",
                            src.substring(Math.max(0, idx - 100),
                                    Math.min(src.length(), idx + 500)));
                }
            }
        }

        softAssert.assertTrue(success,
                "User is not redirected to login callback / auto-login message is not shown. Current URL: "
                        + getDriver().getCurrentUrl());

        softAssert.assertAll();
    }
}
