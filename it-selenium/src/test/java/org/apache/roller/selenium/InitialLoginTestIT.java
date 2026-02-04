package org.apache.roller.selenium;

import org.apache roller.selenium.config.TestConfiguration;
import org.apache roller.selenium.utils.SeleniumUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class InitialLoginTestIT {

    private static final int TIMEOUT_IN_SECONDS = 10;
    private static final int POLLING_INTERVAL_IN_MILLISECONDS = 500;
    private static final String USERNAME_INPUT_ID = "username";
    private static final String PASSWORD_INPUT_ID = "password";
    private static final String SUBMIT_BUTTON_ID = "submit";

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeClass
    public void setup() {
        driver = SeleniumUtils.getWebDriver();
        wait = new WebDriverWait(driver, TIMEOUT_IN_SECONDS, POLLING_INTERVAL_IN_MILLISECONDS);
    }

    @Test
    public void testInitialLogin() {
        navigateToLogin();
        enterCredentials("username", "password");
        submitForm();
        verifyLoginSuccess();
    }

    private void navigateToLogin() {
        driver.get(TestConfiguration.getLoginUrl());
    }

    private void enterCredentials(String username, String password) {
        WebElement usernameInput = wait.until(ExpectedConditions.elementToBeClickable(By.id(USERNAME_INPUT_ID)));
        usernameInput.sendKeys(username);

        WebElement passwordInput = wait.until(ExpectedConditions.elementToBeClickable(By.id(PASSWORD_INPUT_ID)));
        passwordInput.sendKeys(password);
    }

    private void submitForm() {
        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.id(SUBMIT_BUTTON_ID)));
        submitButton.click();
    }

    private void verifyLoginSuccess() {
        wait.until(ExpectedConditions.titleIs("Login Success"));
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}