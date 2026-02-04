package org.apache.roller.selenium;

import org.apache.commons.lang3.StringUtils;
import org.awaitility.Awaitility;
import org.awaitility.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class AbstractRollerPage {

    private static final int WAIT_TIMEOUT = 10; // seconds
    private static final String BLOG_TITLE_LOCATOR = "blogTitle";
    private static final String CREATE_POST_BUTTON_LOCATOR = "createPostButton";
    private static final String POST_TITLE_LOCATOR = "postTitle";
    private static final String POST_CONTENT_LOCATOR = "postContent";
    private static final String SAVE_POST_BUTTON_LOCATOR = "savePostButton";

    protected WebDriver driver;

    public AbstractRollerPage(WebDriver driver) {
        this.driver = driver;
    }

    protected void waitForElementToBeVisible(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected void waitForElementToBeClickable(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
        wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected void waitForTextToBePresentInElement(By locator, String text) {
        WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
        wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    protected void enterTextIntoElement(By locator, String text) {
        WebElement element = driver.findElement(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected void clickElement(By locator) {
        driver.findElement(locator).click();
    }

    protected String getElementText(By locator) {
        return driver.findElement(locator).getText();
    }

    protected boolean isElementPresent(By locator) {
        return driver.findElements(locator).size() > 0;
    }

    public boolean isBlogTitleVisible() {
        return isElementPresent(By.id(BLOG_TITLE_LOCATOR));
    }

    public void clickCreatePostButton() {
        clickElement(By.id(CREATE_POST_BUTTON_LOCATOR));
    }

    public void enterPostTitle(String title) {
        enterTextIntoElement(By.id(POST_TITLE_LOCATOR), title);
    }

    public void enterPostContent(String content) {
        enterTextIntoElement(By.id(POST_CONTENT_LOCATOR), content);
    }

    public void clickSavePostButton() {
        clickElement(By.id(SAVE_POST_BUTTON_LOCATOR));
    }

    public void waitForPostToBeSaved() {
        waitForTextToBePresentInElement(By.id(POST_TITLE_LOCATOR), "Post saved successfully");
    }

    public void createNewPost(String title, String content) {
        clickCreatePostButton();
        enterPostTitle(title);
        enterPostContent(content);
        clickSavePostButton();
        waitForPostToBeSaved();
    }
}