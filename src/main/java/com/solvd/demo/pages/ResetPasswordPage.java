package com.solvd.demo.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class ResetPasswordPage extends AbstractPage {

    @FindBy(xpath = "//h2[normalize-space()='Reset your password']/ancestor::form//input[@name='identifier'] | //input[@id='identifier' and not(@type='hidden')]")
    private ExtendedWebElement emailAddressInput;

    @FindBy(xpath = "//h2[normalize-space()='Reset your password']/ancestor::form//input[@type='submit' and @value='Next'] | //input[@type='submit' and @value='Next'] | //button[normalize-space()='Next']")
    private ExtendedWebElement nextButton;

    public ResetPasswordPage(WebDriver driver) {
        super(driver);
    }

    public void typeEmail(String email) {
        WebDriver driver = getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        // Wait until the Reset your password heading is visible to ensure the form is rendered
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h2[normalize-space()='Reset your password']")));

        // Pick the visible identifier input (login form's identifier may still be in DOM)
        List<WebElement> inputs = driver.findElements(By.cssSelector("input[name='identifier'], #identifier"));
        WebElement target = null;
        for (WebElement i : inputs) {
            if (i.isDisplayed() && i.isEnabled()) {
                target = i;
                break;
            }
        }
        if (target == null && !inputs.isEmpty()) {
            target = inputs.get(inputs.size() - 1);
        }
        if (target == null) {
            emailAddressInput.type(email);
            return;
        }
        // Scroll into view, click to focus, then send real keys (Okta validates via input events)
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'}); arguments[0].focus();", target);
        try {
            target.click();
        } catch (Exception ignored) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", target);
        }
        target.clear();
        target.sendKeys(email);
    }

    public VerifyEmailPage clickNext() {
        try {
            ((JavascriptExecutor) getDriver()).executeScript(
                "const btns = document.querySelectorAll('button, input[type=\"submit\"]');"
                    + "for (const b of btns) { const v = (b.value || b.innerText || '').toLowerCase().trim();"
                    + " if (v === 'next' && b.offsetWidth > 0) { b.click(); return; } }"
            );
        } catch (Exception e) {
            nextButton.click();
        }
        return new VerifyEmailPage(getDriver());
    }

    public boolean isEmailInputPresent() {
        return emailAddressInput.isPresent();
    }

    public boolean isNextButtonPresent() {
        return nextButton.isPresent();
    }
}
