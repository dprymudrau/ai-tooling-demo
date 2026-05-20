package com.solvd.demo;

import com.solvd.demo.pages.AccountPage;
import com.solvd.demo.pages.HomePage;
import com.solvd.demo.pages.LoginPage;
import com.zebrunner.agent.core.annotation.TestCaseKey;
import com.zebrunner.carina.core.IAbstractTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class LoginTest implements IAbstractTest {

    @Test(description = "Login with valid email and valid password redirects user to account page")
    @TestCaseKey("PIL-139")
    public void testLoginWithValidEmailAndValidPasswordRedirectsUserToAccountPage() {
        // Step 0: Open the Under Armour homepage
        HomePage homePage = new HomePage(getDriver());
        homePage.open();
        homePage.assertPageOpened();

        // Step 1: Accept cookies
        homePage.acceptCookies();

        // Step 2: Open Log In page
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.open();

        // Step 3-4: Verify login form is displayed
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(loginPage.isLoginFormPresent(),
                "Login form container is not displayed on the Login page");

        // Step 3: Enter valid email
        loginPage.typeEmail("ua_tester_2026_05b@mailinator.com");

        // Step 4: Enter valid password
        loginPage.typePassword("TestUA!2026Pass");

        // Step 5: Click Log In submit button
        AccountPage accountPage = loginPage.clickLogIn();

        // Step 6: Navigate to account page (or verify redirect)
        accountPage.navigateToAccountPage();

        // Verify URL contains /account (primary success criterion: navigation to /account)
        String currentUrl = accountPage.getCurrentUrl();
        softAssert.assertTrue(currentUrl.contains("/account"),
                "Current URL does not contain '/account'. Actual URL: " + currentUrl);

        softAssert.assertAll();
    }
}
