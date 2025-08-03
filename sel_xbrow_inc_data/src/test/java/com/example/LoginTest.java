package com.example;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.By;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.testng.Assert;

public class LoginTest {

    @DataProvider(name = "loginData")
    public Object[][] loginData() {
        return new Object[][] {
            {"standard_user", "secret_sauce"},
            {"locked_out_user", "secret_sauce"},
            {"problem_user", "secret_sauce"},
            {"performance_glitch_user", "secret_sauce"},
            {"error_user", "secret_sauce"},
            {"visual_user", "secret_sauce"}
        };
    }

    @Test(dataProvider = "loginData")
    public void testLogin(String username, String password) {
        WebDriverManager.firefoxdriver().setup();
        WebDriver driver = new FirefoxDriver();
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
                    "Login should succeed for: " + username);
            } else {
                Assert.assertEquals(currentUrl, 
                    "https://www.saucedemo.com/", 
                    "Login should fail for: " + username);
            }
        } finally {
            driver.quit();
        }
    }
}