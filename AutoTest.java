package test1;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.List;
import java.util.Set;

public class AutoTest {

    WebDriver driver;
    WebDriverWait wait;
    String baseUrl = "https://automationexercise.com";
    String firstName = "srijan";

    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        driver.get(baseUrl);

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement link = driver.findElement(By.linkText("Signup / Login"));
        link.click();
    }

    @Test(priority = 1)
    public void Registration() {
        driver.findElement(By.name("name")).sendKeys("srijan");
        driver.findElement(By.xpath("//*[@data-qa='signup-email']")).sendKeys("srijan222@gmail.com");
        driver.findElement(By.xpath("//button[contains(text(),'Signup')]")).click();

        driver.findElement(By.id("uniform-id_gender1")).click();
        driver.findElement(By.id("password")).sendKeys("Shopping88");

        Select day = new Select(driver.findElement(By.name("days")));
        day.selectByIndex(1);

        Select month = new Select(driver.findElement(By.name("months")));
        month.selectByIndex(1);

        Select year = new Select(driver.findElement(By.name("years")));
        year.selectByIndex(1);

        driver.findElement(By.id("first_name")).sendKeys("abc");
        driver.findElement(By.id("last_name")).sendKeys("xyz");
        driver.findElement(By.id("company")).sendKeys("home");
        driver.findElement(By.id("address1")).sendKeys("ktm");

        Select coun = new Select(driver.findElement(By.name("country")));
        coun.selectByIndex(2);

        driver.findElement(By.id("state")).sendKeys("cal");
        driver.findElement(By.id("city")).sendKeys("la");
        driver.findElement(By.id("zipcode")).sendKeys("+80");
        driver.findElement(By.id("mobile_number")).sendKeys("09876");

        driver.findElement(By.xpath("//button[contains(text(),'Create Account')]")).click();
        driver.findElement(By.linkText("Continue")).click();

        // Check registration success
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".fa.fa-user")));
        Assert.assertTrue(driver.findElement(By.cssSelector(".fa.fa-user")).getText().contains(firstName));

        // Store session cookies
        Set<Cookie> cookies = driver.manage().getCookies();
    }

    @Test(priority = 2)
    public void productFiltering() {
        driver.get(baseUrl + "/products");

        driver.findElement(By.linkText("Women")).click();
        driver.findElement(By.linkText("Dress")).click();

        List<WebElement> products = driver.findElements(By.className("product-information"));

        boolean hasDress = products.stream().anyMatch(p -> p.getText().toLowerCase().contains("dress"));
        Assert.assertTrue(hasDress);
        products.get(0).click();

        WebElement productName = driver.findElement(By.cssSelector(".product-information h2"));
        WebElement productPrice = driver.findElement(By.cssSelector(".product-information span span"));
        WebElement availability = driver.findElement(By.xpath("//div[@class='product-information']/p/b[contains(text(),'Availability')]/parent::p"));

        Assert.assertTrue(productName.isDisplayed());
        Assert.assertTrue(productPrice.isDisplayed());
        Assert.assertTrue(availability.getText().contains("In Stock"));
    }

    @Test(priority = 3)
    public void cartManagement() {
        driver.get(baseUrl + "/products");

        driver.findElement(By.linkText("Women")).click();
        driver.findElement(By.linkText("Dress")).click();

        WebElement firstAddToCart = driver.findElement(By.cssSelector(".btn.btn-default.cart"));
        firstAddToCart.click();

        driver.findElement(By.linkText("Men")).click();

        WebElement quantityInput = driver.findElement(By.id("quantity"));
        quantityInput.clear();
        quantityInput.sendKeys("3");

        WebElement secondAddToCart = driver.findElement(By.cssSelector(".btn.btn-default.cart"));
        secondAddToCart.click();

        driver.findElement(By.cssSelector("a[href='/view_cart']")).click();

        // Delete item with data-product-id="2"
        driver.findElement(By.cssSelector(".cart_delete [data-product-id='2']")).click();
    }

    @Test(priority = 4)
    public void checkoutFlowWithFakePayment() {
        driver.findElement(By.linkText("Proceed To Checkout")).click();
        driver.findElement(By.name("message")).sendKeys("Please deliver it soon!");
        driver.findElement(By.linkText("Place Order")).click();

        driver.findElement(By.name("name_on_card")).sendKeys("SRZ");
        driver.findElement(By.name("card_number")).sendKeys("657");
        driver.findElement(By.name("cvc")).sendKeys("099");
        driver.findElement(By.name("expiry_month")).sendKeys("12");
        driver.findElement(By.name("expiry_year")).sendKeys("2028");
        driver.findElement(By.id("submit")).click();

        WebElement confirmationElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(), 'Congratulations! Your order has been confirmed!')]")));
        Assert.assertTrue(confirmationElement.getText().contains("Congratulations! Your order has been confirmed!"));
    }

    @Test(priority = 5)
    public void logoutAndlogin() {
        driver.findElement(By.cssSelector("a[href='/logout']")).click();

        // Login again
        driver.findElement(By.id("email")).sendKeys("srijan222@gmail.com");
        driver.findElement(By.id("password")).sendKeys("Shopping88");
        driver.findElement(By.xpath("//*[@data-qa='login-button']")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".fa.fa-user")));
        Assert.assertTrue(driver.findElement(By.cssSelector(".fa.fa-user")).getText().contains(firstName));
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}
