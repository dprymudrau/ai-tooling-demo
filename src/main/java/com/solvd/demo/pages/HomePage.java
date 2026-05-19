package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;

public class HomePage extends AbstractPage {

    private static final String HOME_PAGE_URL = "https://www.underarmour.com/en-us/";

    public HomePage(WebDriver driver) {
        super(driver);
        setPageAbsoluteURL(HOME_PAGE_URL);
    }
}
