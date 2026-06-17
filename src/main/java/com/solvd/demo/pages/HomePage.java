package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class HomePage extends AbstractPage {

    @FindBy(id = "header")
    private ExtendedWebElement headerBlock;

    @FindBy(css = "#nav-tabs a, .nav-tab a")
    private List<ExtendedWebElement> topNavLinks;

    @FindBy(xpath = "//div[contains(@class,'brandmenu-v2')]//a")
    private List<ExtendedWebElement> brandLinks;

    @FindBy(css = "div.module-latest, #latest-news, #news-list")
    private ExtendedWebElement contentModule;

    public HomePage(WebDriver driver) {
        super(driver);
        setPageAbsoluteURL("https://www.gsmarena.com/");
    }

    public boolean isHeaderPresent() {
        return headerBlock.isElementPresent(30);
    }

    public boolean hasBrands() {
        return !brandLinks.isEmpty();
    }

    public int countBrands() {
        return brandLinks.size();
    }
}
