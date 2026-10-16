package com.solvd.demo;

import com.solvd.demo.pages.HomePage;
import com.solvd.demo.pages.LoginPage;
import com.solvd.demo.pages.ResetPasswordPage;
import com.solvd.demo.pages.VerifyEmailConfirmationPage;
import com.solvd.demo.pages.VerifyEmailPage;
import com.solvd.demo.utils.GmailReader;
import com.zebrunner.agent.core.annotation.TestCaseKey;
import com.zebrunner.carina.core.IAbstractTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class ForgotPasswordEmailTest implements IAbstractTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ForgotPasswordEmailTest.class);

    private static final String HOME_URL = "https://www.underarmour.com/en-us/";
    private static final String LOGIN_URL = "https://www.underarmour.com/en-us/login";
    private static final String REGISTERED_EMAIL = "dmitryprimudriv+1@gmail.com";
    private static final String MAILBOX_ADDRESS = "dmitryprimudriv@gmail.com";
    private static final String MAILBOX_APP_PASSWORD = "ympm vgzi sufa sxeo";
    private static final String EXPECTED_EMAIL_SUBJECT = "Password Reset Request for Your UA.com Account";
    private static final String EXPECTED_HEADING = "Verify with your email";
    private static final int EMAIL_TIMEOUT_SECONDS = 120;

    @Test(description = "Request password reset email from Forgot Password flow")
    @TestCaseKey("PLTV2-1152")
    public void testRequestPasswordResetEmail() {
        SoftAssert softAssert = new SoftAssert();

        // Step 0: Open Under Armour homepage
        LOGGER.info("Step 0: Navigate to Under Armour homepage");
        HomePage homePage = new HomePage(getDriver());
        getDriver().get(HOME_URL);

        // Step 1: Accept cookies
        LOGGER.info("Step 1: Accept cookies");
        softAssert.assertTrue(homePage.isAcceptCookiesButtonPresent(),
                "Cookie consent banner is not displayed.");
        homePage.acceptCookiesIfPresent();

        // Step 2: Navigate to Login page
        LOGGER.info("Step 2: Navigate to Login page");
        getDriver().get(LOGIN_URL);
        LoginPage loginPage = new LoginPage(getDriver());
        softAssert.assertTrue(loginPage.isForgotPasswordLinkPresent(),
                "Forgot Password link is not present on the Login page.");

        // Step 3: Click the Forgot Password link
        LOGGER.info("Step 3: Click Forgot Password link");
        ResetPasswordPage resetPasswordPage = loginPage.clickForgotPasswordLink();
        softAssert.assertTrue(resetPasswordPage.isEmailInputPresent(),
                "Email input field is not displayed on the Reset Password page.");
        softAssert.assertTrue(resetPasswordPage.isNextButtonPresent(),
                "Next button is not displayed on the Reset Password page.");

        // Step 4: Enter registered email
        LOGGER.info("Step 4: Enter registered email '{}'", REGISTERED_EMAIL);
        resetPasswordPage.enterEmail(REGISTERED_EMAIL);
        String typedValue = resetPasswordPage.getEmailValue();
        softAssert.assertEquals(typedValue, REGISTERED_EMAIL,
                "Email field does not contain the entered value.");

        // Step 5: Click Next button
        LOGGER.info("Step 5: Click Next button");
        VerifyEmailPage verifyEmailPage = resetPasswordPage.clickNext();
        softAssert.assertTrue(verifyEmailPage.isSendMeAnEmailButtonPresent(),
                "'Send me an email' button is not displayed on the intermediary screen.");

        // Step 6: Click "Send me an email" button
        LOGGER.info("Step 6: Click 'Send me an email' button");
        VerifyEmailConfirmationPage confirmationPage = verifyEmailPage.clickSendMeAnEmail();

        // Step 7 (web): Validate confirmation heading
        LOGGER.info("Step 7: Validate confirmation page heading");
        softAssert.assertTrue(confirmationPage.isVerifyWithYourEmailHeadingPresent(),
                "'Verify with your email' heading is not displayed.");
        String actualHeading = confirmationPage.getVerifyWithYourEmailHeadingText();
        softAssert.assertTrue(actualHeading != null && actualHeading.contains(EXPECTED_HEADING),
                "Confirmation heading text mismatch. Actual: " + actualHeading);

        // Step 7 (email): Verify password reset email arrived
        LOGGER.info("Step 8: Fetch password reset email for '{}'", REGISTERED_EMAIL);
        GmailReader gmailReader = new GmailReader(MAILBOX_ADDRESS, MAILBOX_APP_PASSWORD);
        String emailBody = gmailReader.waitForEmail(REGISTERED_EMAIL, EXPECTED_EMAIL_SUBJECT,
                EMAIL_TIMEOUT_SECONDS);
        Assert.assertNotNull(emailBody, "Password reset email was not received within "
                + EMAIL_TIMEOUT_SECONDS + " seconds.");

        String resetLink = GmailReader.extractResetPasswordLink(emailBody);
        softAssert.assertNotNull(resetLink,
                "Reset password link not found in the email body.");
        if (resetLink != null) {
            softAssert.assertTrue(resetLink.startsWith("https://login.shop.underarmour.com/email/verify/"),
                    "Reset link host is invalid. Actual: " + resetLink);
        }
        softAssert.assertTrue(emailBody.toLowerCase().contains("30 minutes"),
                "Email does not mention link expiration in 30 minutes.");

        softAssert.assertAll();
    }
}
