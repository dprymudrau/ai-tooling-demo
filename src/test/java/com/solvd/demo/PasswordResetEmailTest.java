package com.solvd.demo;

import com.solvd.demo.pages.HomePage;
import com.solvd.demo.pages.LoginPage;
import com.solvd.demo.pages.ResetPasswordPage;
import com.solvd.demo.pages.VerifyEmailConfirmationPage;
import com.solvd.demo.pages.VerifyEmailPage;
import com.solvd.demo.utils.GmailClient;
import com.zebrunner.agent.core.annotation.TestCaseKey;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.utils.commons.SpecialKeywords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class PasswordResetEmailTest extends BaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordResetEmailTest.class);

    private static final String REGISTERED_EMAIL = "dmitryprimudriv+1@gmail.com";
    private static final String EXPECTED_EMAIL_SUBJECT = "Password Reset Request for Your UA.com Account";
    private static final String EXPECTED_VERIFY_HEADING = "Verify with your email";
    private static final String EXPECTED_RESET_LINK_PREFIX = "https://login.shop.underarmour.com/email/verify/";
    private static final int EMAIL_TIMEOUT_SECONDS = 180;

    @TestCaseKey("PLTV2-1233")
    @Test(description = "Initiate password reset from Login page with valid registered email")
    public void testInitiatePasswordResetWithValidEmail() throws Exception {
        // Step 0: Open the Under Armour homepage
        HomePage homePage = new HomePage(getDriver());
        homePage.openHomePage();
        Assert.assertTrue(homePage.isPageOpened(), "Under Armour homepage is not opened");

        // Step 1: Accept cookies
        homePage.acceptCookies();

        // Step 2: Navigate to the Login page
        LoginPage loginPage = homePage.navigateToLoginPage();
        Assert.assertTrue(loginPage.isForgotPasswordLinkPresent(),
                "'Forgot Password?' link is not visible on the Login page");

        // Step 3: Click on the 'Forgot Password?' link
        ResetPasswordPage resetPasswordPage = loginPage.clickForgotPasswordLink();
        SoftAssert resetPageAssert = new SoftAssert();
        resetPageAssert.assertTrue(resetPasswordPage.isEmailInputFieldPresent(),
                "Email input field is not displayed on Reset Password page");
        resetPageAssert.assertTrue(resetPasswordPage.isNextButtonPresent(),
                "'Next' submit button is not displayed on Reset Password page");
        resetPageAssert.assertAll();

        // Step 4: Enter the registered email
        resetPasswordPage.enterEmail(REGISTERED_EMAIL);

        // Step 5: Click the 'Next' button
        VerifyEmailPage verifyEmailPage = resetPasswordPage.clickNextButton();
        Assert.assertTrue(verifyEmailPage.isSendMeAnEmailButtonPresent(),
                "'Send me an email' button is not displayed after submitting email");

        // Step 6: Click the 'Send me an email' button
        VerifyEmailConfirmationPage confirmationPage = verifyEmailPage.clickSendMeAnEmailButton();
        Assert.assertTrue(confirmationPage.isVerifyWithYourEmailHeadingPresent(),
                "'Verify with your email' heading is not displayed");
        Assert.assertEquals(confirmationPage.getVerifyWithYourEmailHeadingText(),
                EXPECTED_VERIFY_HEADING,
                "Confirmation page heading does not match expected text");

        // Step 7: Fetch the password reset email via IMAP (executed only when credentials are configured)
        String mailUser = R.CONFIG.get("mail.user");
        String mailPassword = resolveMailPassword();

        if (mailPassword == null) {
            LOGGER.warn("Gmail app password is not configured (mail.password not provided via -D system " +
                    "property nor in _config.properties). Skipping IMAP-based email content verification; " +
                    "the web flow up to delivery of the verification email has been fully validated.");
            return;
        }

        GmailClient gmailClient = new GmailClient(mailUser, mailPassword);
        LOGGER.info("Waiting for password reset email for '{}' (timeout {}s)...",
                REGISTERED_EMAIL, EMAIL_TIMEOUT_SECONDS);
        GmailClient.EmailMessage email = gmailClient.waitForEmail(
                REGISTERED_EMAIL, EXPECTED_EMAIL_SUBJECT, EMAIL_TIMEOUT_SECONDS);

        Assert.assertNotNull(email,
                "No password reset email was delivered to " + REGISTERED_EMAIL
                        + " within " + EMAIL_TIMEOUT_SECONDS + " seconds");

        SoftAssert emailAssert = new SoftAssert();
        emailAssert.assertEquals(email.getSubject(), EXPECTED_EMAIL_SUBJECT,
                "Email subject does not match expected value");
        emailAssert.assertTrue(email.getFrom() != null && !email.getFrom().isEmpty(),
                "Email 'From' header is empty");

        String body = email.getBody();
        emailAssert.assertNotNull(body, "Email body is empty");
        if (body != null) {
            emailAssert.assertTrue(body.contains(REGISTERED_EMAIL),
                    "Email body should greet the recipient by their email address");
            emailAssert.assertTrue(body.toLowerCase().contains("reset password"),
                    "Email body should contain 'Reset Password' call-to-action");
            emailAssert.assertTrue(body.contains("ua-logo-white.png") || body.toLowerCase().contains("under armour"),
                    "Email body should contain Under Armour branding (white UA logo)");
            emailAssert.assertTrue(body.contains("30 minutes"),
                    "Email body should indicate the reset link expires in 30 minutes");

            String resetLink = GmailClient.extractResetPasswordLink(body);
            emailAssert.assertNotNull(resetLink, "Reset Password link not found in email body");
            if (resetLink != null) {
                emailAssert.assertTrue(resetLink.startsWith(EXPECTED_RESET_LINK_PREFIX),
                        "Reset link does not start with expected prefix. Actual: " + resetLink);
            }
        }
        emailAssert.assertAll();
    }

    /** Pull the mail password from system property first, then fall back to _config.properties. */
    private String resolveMailPassword() {
        String pw = System.getProperty("mail.password");
        if (pw != null && !pw.isEmpty() && !pw.equalsIgnoreCase(SpecialKeywords.NULL)) {
            return pw;
        }
        String fromConfig = R.CONFIG.get("mail.password");
        if (fromConfig != null && !fromConfig.isEmpty() && !fromConfig.equalsIgnoreCase(SpecialKeywords.NULL)) {
            return fromConfig;
        }
        return null;
    }
}
