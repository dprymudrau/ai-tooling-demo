package com.solvd.demo;

import com.solvd.demo.pages.ArmourAccessSidePanel;
import com.solvd.demo.pages.HomePage;
import com.solvd.demo.pages.LoginPage;
import com.zebrunner.agent.core.annotation.TestCaseKey;
import com.zebrunner.carina.core.registrar.ownership.MethodOwner;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class LoginFormVisibilityTest extends BaseTest {

    @Test(description = "Open login form on Under Armour and validate it is visible")
    @TestCaseKey("PLT-153")
    @MethodOwner(owner = "qaagent")
    public void testOpenLoginFormIsVisible() {
        // Step 0: Open Under Armour home page
        HomePage homePage = new HomePage(getDriver());
        homePage.open();

        // Step 1: Click the 'Log In or Join' control in the site header
        ArmourAccessSidePanel sidePanel = homePage.openLogInOrJoinPanel();

        // Step 2: Click the 'Log In' button inside the Armour Access side panel
        LoginPage loginPage = sidePanel.clickLogIn();

        // Step 2/3: Verify login form card and key elements are displayed and visible
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(loginPage.getLoginFormCard().isElementPresent(),
                "Login form card is not visible");
        softAssert.assertTrue(loginPage.getArmourAccessLogo().isElementPresent(),
                "Armour Access logo is not visible");
        softAssert.assertTrue(loginPage.getLogInHeading().isElementPresent(),
                "'Log In' heading is not visible");
        softAssert.assertTrue(loginPage.getEmailAddressField().isElementPresent(),
                "'Email Address' field is not visible");
        softAssert.assertTrue(loginPage.getPasswordField().isElementPresent(),
                "'Password' field is not visible");
        softAssert.assertTrue(loginPage.getLogInSubmitButton().isElementPresent(),
                "'Log In' submit button is not visible");
        softAssert.assertAll();
    }
}
