package com.example;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.By;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.testng.Assert;

public class LoginTest {

    @DataProvider(name = "loginData")
    public Object[][] loginData() {
        return new Object[][] {
            {"firefox", "standard_user", "secret_sauce", "https://www.saucedemo.com/inventory.html"},
            {"firefox", "locked_out_user", "secret_sauce", "https://www.saucedemo.com/"},
            {"firefox", "problem_user", "secret_sauce", "https://www.saucedemo.com/inventory.html"},
            {"firefox", "performance_glitch_user", "secret_sauce", "https://www.saucedemo.com/inventory.html"},
            {"firefox", "error_user", "secret_sauce", "https://www.saucedemo.com/inventory.html"},
            {"firefox", "visual_user", "secret_sauce", "https://www.saucedemo.com/inventory.html"},
            {"chrome", "standard_user", "secret_sauce", "https://www.saucedemo.com/inventory.html"},
            {"chrome", "locked_out_user", "secret_sauce", "https://www.saucedemo.com/"},
            {"chrome", "problem_user", "secret_sauce", "https://www.saucedemo.com/inventory.html"},
            {"chrome", "performance_glitch_user", "secret_sauce", "https://www.saucedemo.com/inventory.html"},
            {"chrome", "error_user", "secret_sauce", "https://www.saucedemo.com/inventory.html"},
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
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}