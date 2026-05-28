package com.solvd.demo;

import com.solvd.demo.pages.HomePage;
import com.zebrunner.carina.core.IAbstractTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HomePageTest implements IAbstractTest {

    @Test
    public void testHomePageOpens() {
        HomePage homePage = new HomePage(getDriver());
        homePage.open();
        Assert.assertTrue(homePage.isPageOpened(), "GSM Arena home page is not opened");
        Assert.assertTrue(homePage.isHeaderPresent(), "Header element is not visible on home page");
    }

    @Test
    public void testHomePageHasBrands() {
        HomePage homePage = new HomePage(getDriver());
        homePage.open();
        Assert.assertTrue(homePage.isPageOpened(), "GSM Arena home page is not opened");
        Assert.assertTrue(homePage.hasBrands(),
                "Brand list should not be empty on home page");
        Assert.assertTrue(homePage.countBrands() > 5,
                "Expected more than 5 brand links on the home page");
    }
}
