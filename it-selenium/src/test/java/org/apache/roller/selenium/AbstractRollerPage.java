package org.apache.roller.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Base class for all Roller page objects.
 */
public abstract class AbstractRollerPage {

    private static final int DEFAULT_TIMEOUT = 10;
    private static final String LOGIN_FORMLocator = "form[name='loginForm']";
    private static final String USERNAME_INPUT_LOCATOR = "input[name='username']";
    private static final String PASSWORD_INPUT_LOCATOR = "input[name='password']";
    private static final String SUBMIT_BUTTON_LOCATOR = "input[type='submit']";

    protected WebDriver driver;

    public AbstractRollerPage(WebDriver driver) {
        this.driver = driver;
    }

    public void login(String username, String password) {
        WebElement loginForm = driver.findElement(By.cssSelector(LOGIN_FORMLocator));
        WebElement usernameInput = loginForm.findElement(By.cssSelector(USERNAME_INPUT_LOCATOR));
        WebElement passwordInput = loginForm.findElement(By.cssSelector(PASSWORD_INPUT_LOCATOR));
        WebElement submitButton = loginForm.findElement(By.cssSelector(SUBMIT_BUTTON_LOCATOR));

        usernameInput.sendKeys(username);
        passwordInput.sendKeys(password);
        submitButton.click();
    }

    public boolean waitForElementVisible(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, DEFAULT_TIMEOUT);
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean waitForElementInvisible(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, DEFAULT_TIMEOUT);
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}