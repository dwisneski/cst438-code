package com.cst438.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CustomerControllerSystemTest {

    static final String CHROME_DRIVER_FILE_LOCATION = "C:/chromedriver_win64/chromedriver.exe";
    static final String URL = "http://localhost:5173";   // react dev server

    static final int DELAY = 2000;
    WebDriver driver;

    Wait<WebDriver> wait;

    Random random = new Random();

    @BeforeEach
    public void setUpDriver() throws Exception {

        // set properties required by Chrome Driver
        System.setProperty(
                "webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
        ChromeOptions ops = new ChromeOptions();
        ops.addArguments("--remote-allow-origins=*");

        // start the driver
        driver = new ChromeDriver(ops);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        driver.get(URL);
    }

    @AfterEach
    public void quit() {
        driver.quit();
    }

    // test register, login, create order, view order history
    @Test
    public void createOrder() throws InterruptedException {
        Alert alert;
        int randomInt = random.nextInt(100, 1000);
        String email = "test"+randomInt+"@csumb.edu";
        // register
        driver.findElement(By.id("register")).click();
        driver.findElement(By.id("name")).sendKeys("testonetwothree");
        driver.findElement(By.id("email")).sendKeys(email);
        driver.findElement(By.id("password")).sendKeys("password123");
        driver.findElement(By.id("registerButton")).click();

        // wait for and dismiss the alert message
        wait.until(ExpectedConditions.alertIsPresent());
        alert = driver.switchTo().alert();
        alert.accept();
        Thread.sleep(DELAY);

        // login
        driver.findElement(By.id("home")).click();
        driver.findElement(By.id("email")).sendKeys(email);
        driver.findElement(By.id("password")).sendKeys("password123");
        driver.findElement(By.id("loginButton")).click();

        // dismiss the alert message
        wait.until(ExpectedConditions.alertIsPresent());
        alert = driver.switchTo().alert();
        alert.accept();
        Thread.sleep(DELAY);

        // create order
        driver.findElement(By.id("order")).click();
        driver.findElement(By.id("item")).sendKeys("item123");
        driver.findElement(By.id("quantity")).sendKeys("1");
        driver.findElement(By.id("price")).sendKeys("9.99");
        driver.findElement(By.id("orderButton")).click();

        // dismiss alert message
        wait.until(ExpectedConditions.alertIsPresent());
        alert = driver.switchTo().alert();
        alert.accept();
        Thread.sleep(DELAY);

        // order history
        driver.findElement(By.id("history")).click();
        // check that order item123 is listed in the table
        WebElement we = driver.findElement(By.xpath("//td[text()='item123']"));
        assertNotNull(we);

    }

    @Test
    public void createAndEditOrder() throws InterruptedException {
        Alert alert;
        int randomInt = random.nextInt(100, 1000);
        String email = "test"+randomInt+"@csumb.edu";
        // register
        driver.findElement(By.id("register")).click();
        driver.findElement(By.id("name")).sendKeys("testonetwothree");
        driver.findElement(By.id("email")).sendKeys(email);
        driver.findElement(By.id("password")).sendKeys("password123");
        driver.findElement(By.id("registerButton")).click();

        // wait for and dismiss the alert message
        wait.until(ExpectedConditions.alertIsPresent());
        alert = driver.switchTo().alert();
        alert.accept();
        Thread.sleep(DELAY);

        // login
        driver.findElement(By.id("home")).click();
        driver.findElement(By.id("email")).sendKeys(email);
        driver.findElement(By.id("password")).sendKeys("password123");
        driver.findElement(By.id("loginButton")).click();

        // dismiss the alert message
        wait.until(ExpectedConditions.alertIsPresent());
        alert = driver.switchTo().alert();
        alert.accept();
        Thread.sleep(DELAY);

        // create order
        driver.findElement(By.id("order")).click();
        driver.findElement(By.id("item")).sendKeys("testItem");
        driver.findElement(By.id("quantity")).sendKeys("1");
        driver.findElement(By.id("price")).sendKeys("9.99");
        driver.findElement(By.id("orderButton")).click();

        // dismiss alert message
        wait.until(ExpectedConditions.alertIsPresent());
        alert = driver.switchTo().alert();
        alert.accept();
        Thread.sleep(DELAY);

        // find the order in order history
        driver.findElement(By.id("history")).click();
        // check that order item123 is listed in the table
        WebElement we = driver.findElement(By.xpath("//tr[./td[text()='testItem']]"));
        assertNotNull(we);
        // click the edit button for the test item
        we.findElement(By.xpath("//button[text()='Edit']")).click();
        // change quantity to 10 and click save
        we = driver.findElement(By.id("quantity"));
        we.clear();
        we.sendKeys("10");
        driver.findElement(By.id("saveButton")).click();
        // verify that the quantity 10 appears on the order history list
        we = driver.findElement(By.xpath("//tr/td[text()='10']"));
        assertNotNull(we, "new quantity does not appear on history page after edit and save");

    }

    // test register, login, attempt to create bad order with zero quantity
    @Test
    public void createBadOrder() throws InterruptedException {
        Alert alert;
        int randomInt = random.nextInt(100, 1000);
        String email = "test"+randomInt+"@csumb.edu";
        // register
        driver.findElement(By.id("register")).click();
        driver.findElement(By.id("name")).sendKeys("testonetwothree");
        driver.findElement(By.id("email")).sendKeys(email);
        driver.findElement(By.id("password")).sendKeys("password123");
        driver.findElement(By.id("registerButton")).click();
        // dismiss the alert message
        wait.until(ExpectedConditions.alertIsPresent());
        alert = driver.switchTo().alert();
        alert.accept();
        Thread.sleep(DELAY);

        // login
        driver.findElement(By.id("home")).click();
        driver.findElement(By.id("email")).sendKeys(email);
        driver.findElement(By.id("password")).sendKeys("password123");
        driver.findElement(By.id("loginButton")).click();
        // dismiss the alert message
        wait.until(ExpectedConditions.alertIsPresent());
        alert = driver.switchTo().alert();
        alert.accept();
        Thread.sleep(DELAY);

        // create order
        driver.findElement(By.id("order")).click();
        driver.findElement(By.id("item")).sendKeys("item123");
        driver.findElement(By.id("quantity")).sendKeys("0");
        driver.findElement(By.id("price")).sendKeys("9.99");
        driver.findElement(By.id("orderButton")).click();
        // verify error message item quantity can not be 0
        WebElement we = driver.findElement(By.xpath("//p[text()='quantity must be positive']"));
        assertNotNull(we);

    }

    // attempt to register with bad email
    @Test
    public void registerBadName() {

        Alert alert;
        int randomInt = random.nextInt(100, 1000);
        String email = "test"+randomInt+"@csumb.edu";
        // register
        driver.findElement(By.id("register")).click();
        // name can only be letters, not digits
        driver.findElement(By.id("name")).sendKeys("test 123");
        driver.findElement(By.id("email")).sendKeys(email);
        driver.findElement(By.id("password")).sendKeys("password123");
        driver.findElement(By.id("registerButton")).click();
        // dismiss the alert message
        wait.until(ExpectedConditions.alertIsPresent());
        alert = driver.switchTo().alert();
        String alertMessage = alert.getText();
        assertEquals("Registration failed. 401", alertMessage);
    }
}
