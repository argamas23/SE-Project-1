package org.apache.roller.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class InitialLoginTestIT {

    private static final String USERNAME_INPUT = "username";
    private static final String PASSWORD_INPUT = "password";
    private static final String LOGIN_BUTTON = "loginButton";
    private static final String WELCOME_MESSAGE = "Welcome,";
    private static final long TIME_OUT = 10;

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeClass
    public void setup() {
        driver = SeleniumUtils.getDriver();
        wait = new WebDriverWait(driver, TIME_OUT);
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void testInitialLogin() {
        navigateToLoginPage();
        enterCredentials();
        submitLoginForm();
        verifyLoginSuccess();
    }

    private void navigateToLoginPage() {
        driver.get("http://localhost:8080/roller");
        wait.until(ExpectedConditions.titleContains("Roller"));
    }

    private void enterCredentials() {
        WebElement usernameField = wait.until(ExpectedConditions.elementToBeClickable(By.name(USERNAME_INPUT)));
        WebElement passwordField = wait.until(ExpectedConditions.elementToBeClickable(By.name(PASSWORD_INPUT)));

        usernameField.sendKeys("admin");
        passwordField.sendKeys("password");
    }

    private void submitLoginForm() {
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.name(LOGIN_BUTTON)));
        loginButton.click();
    }

    private void verifyLoginSuccess() {
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("h1"), WELCOME_MESSAGE));
    }
}