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
            {"firefox", "standard_user", "secret_sauce"},
            {"firefox", "locked_out_user", "secret_sauce"},
            {"firefox", "problem_user", "secret_sauce"},
            {"firefox", "performance_glitch_user", "secret_sauce"},
            {"firefox", "error_user", "secret_sauce"},
            {"firefox", "visual_user", "secret_sauce"},
            {"chrome", "standard_user", "secret_sauce"},
            {"chrome", "locked_out_user", "secret_sauce"},
            {"chrome", "problem_user", "secret_sauce"},
            {"chrome", "performance_glitch_user", "secret_sauce"},
            {"chrome", "error_user", "secret_sauce"},
            {"chrome", "visual_user", "secret_sauce"}
        };
    }

    @Test(dataProvider = "loginData")
    public void testLogin(String browser, String username, String password) {
        WebDriver driver = null;
        if ("firefox".equalsIgnoreCase(browser)) {
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
        } else if ("chrome".equalsIgnoreCase(browser)) {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
        } else {
            throw new IllegalArgumentException("Unsupported browser: " + browser);
        }

        try {
            driver.get("https://www.saucedemo.com/");
            driver.findElement(By.id("user-name")).sendKeys(username);
            driver.findElement(By.id("password")).sendKeys(password);
            driver.findElement(By.id("login-button")).click();

            String currentUrl = driver.getCurrentUrl();

            if ("standard_user".equals(username) 
                || "problem_user".equals(username)
                || "performance_glitch_user".equals(username) 
                || "error_user".equals(username) 
                || "visual_user".equals(username)) {
                Assert.assertEquals(currentUrl, 
                    "https://www.saucedemo.com/inventory.html", 
                    "Login should succeed for: " + username + " on " + browser);
            } else {
                Assert.assertEquals(currentUrl, 
                    "https://www.saucedemo.com/", 
                    "Login should fail for: " + username + " on " + browser);
            }
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}