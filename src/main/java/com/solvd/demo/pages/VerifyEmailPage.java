package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class VerifyEmailPage extends AbstractPage {

    @FindBy(xpath = "//button[normalize-space()='Send me an email'] | //a[normalize-space()='Send me an email']")
    private ExtendedWebElement sendMeAnEmailButton;

    public VerifyEmailPage(WebDriver driver) {
        super(driver);
    }

    public VerifyEmailConfirmationPage clickSendMeAnEmail() {
        try {
            ((JavascriptExecutor) getDriver()).executeScript(
                "const all = document.querySelectorAll('button, a, input[type=\"submit\"]');"
                    + "for (const el of all) { const t = (el.innerText || el.value || '').toLowerCase().trim();"
                    + " if (t === 'send me an email' && el.offsetWidth > 0) { el.click(); return; } }"
            );
        } catch (Exception e) {
            sendMeAnEmailButton.click();
        }
        return new VerifyEmailConfirmationPage(getDriver());
    }

    public boolean isSendMeAnEmailButtonPresent() {
        return sendMeAnEmailButton.isPresent();
    }
}
