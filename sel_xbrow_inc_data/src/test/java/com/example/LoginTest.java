package com.example;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.By;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.testng.Assert;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class LoginTest {

    @DataProvider(name = "loginData")
    public Object[][] loginData() {
        return new Object[][] {
            {"firefox", "standard_user", "secret_sauce", "https://www.saucedemo.com/inventory.html"},
            {"chrome", "standard_user", "secret_sauce", "https://www.saucedemo.com/inventory.html"},
            {"firefox", "locked_out_user", "secret_sauce", "https://www.saucedemo.com/"},
            {"chrome", "locked_out_user", "secret_sauce", "https://www.saucedemo.com/"},
/*
            {"firefox", "problem_user", "secret_sauce", "https://www.saucedemo.com/inventory.html"},
            {"chrome", "problem_user", "secret_sauce", "https://www.saucedemo.com/inventory.html"},
            {"firefox", "performance_glitch_user", "secret_sauce", "https://www.saucedemo.com/inventory.html"},
            {"chrome", "performance_glitch_user", "secret_sauce", "https://www.saucedemo.com/inventory.html"},
            {"firefox", "error_user", "secret_sauce", "https://www.saucedemo.com/inventory.html"},
            {"chrome", "error_user", "secret_sauce", "https://www.saucedemo.com/inventory.html"},
 */
            {"firefox", "visual_user", "secret_sauce", "https://www.saucedemo.com/inventory.html"},
            {"chrome", "visual_user", "secret_sauce", "https://www.saucedemo.com/inventory.html"}
        };
    }

    private WebDriver createWebDriver(String browser) {
        if ("firefox".equalsIgnoreCase(browser)) {
            WebDriverManager.firefoxdriver().setup();
            return new FirefoxDriver();
        } else if ("chrome".equalsIgnoreCase(browser)) {
            WebDriverManager.chromedriver().setup();
            return new ChromeDriver();
        } else {
            throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
    }

    private void takeScreenshot(WebDriver driver, String browser, String username) {
        File screenshotsDir = new File("screenshots");
        if (!screenshotsDir.exists()) {
            screenshotsDir.mkdir();
        }
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String filename = String.format("screenshots/SShot_%s_%s_%s.png", browser, username, System.currentTimeMillis());
        File destFile = new File(filename);
        try {
            Files.copy(screenshot.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (java.io.IOException e) {
            e.printStackTrace();
            Assert.fail("Failed to save screenshot: " + e.getMessage());
        }
    }

    @Test(dataProvider = "loginData")
    public void testLogin(String browser, String username, String password, String expectedUrl) {
        WebDriver driver = createWebDriver(browser);

        try {
            driver.get("https://www.saucedemo.com/");
            driver.findElement(By.id("user-name")).sendKeys(username);
            driver.findElement(By.id("password")).sendKeys(password);
            driver.findElement(By.id("login-button")).click();

            String currentUrl = driver.getCurrentUrl();
            Assert.assertEquals(currentUrl, expectedUrl, 
                "Login result for: " + username + " on " + browser);

            // Take screenshot using reusable method
            takeScreenshot(driver, browser, username);
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}