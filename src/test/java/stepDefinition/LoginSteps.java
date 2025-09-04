package stepDefinition;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.cucumber.java.en.*;

public class LoginSteps {
    WebDriver driver;
    WebDriverWait wait;

    @Given("I launch the browser")
    public void i_launch_the_browser() throws InterruptedException {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Thread.sleep(1000); 
    }

    @When("I open the login page")
    public void i_open_the_login_page() throws InterruptedException {
        driver.get("https://www.amazon.in/");
        wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-link-accountList"))).click();
        Thread.sleep(1000);
    }

    @When("I enter valid UserName {string} and password {string}")
    public void i_enter_valid_user_name_and_password(String username, String password) throws InterruptedException {

        driver.findElement(By.id("ap_email_login")).sendKeys(username);
    

        Thread.sleep(800);
        driver.findElement(By.id("continue")).click();
        Thread.sleep(1200);

       
        driver.findElement(By.id("ap_password")).sendKeys(password);
 
        Thread.sleep(800);
    }

    @When("I click on login button")
    public void i_click_on_login_button() throws InterruptedException {
        wait.until(ExpectedConditions.elementToBeClickable(By.id("signInSubmit"))).click();
        Thread.sleep(1500);
    }

    @Then("I should be navigated to the home page")
    public void i_should_be_navigated_to_the_home_page() throws InterruptedException {
        try {
            if (driver.getTitle().contains("Amazon")) {
                System.out.println("Login Successful - Home page displayed.");
            } else {
                System.out.println("Login Failed.");
            }
        } finally {
            Thread.sleep(500);
            driver.quit();
        }
    }

    @Given("I open the browser and launch the login page")
    public void i_open_the_browser_and_launch_the_login_page() throws InterruptedException {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Thread.sleep(1000);
        driver.get("https://www.amazon.in/");
        Thread.sleep(1500);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-link-accountList"))).click();
        Thread.sleep(1000);
    }

    @When("I login with userName {string} and password {string}")
    public void i_login_with_user_name_and_password(String username, String password) throws InterruptedException {

        if (username == null || username.isEmpty()) {
            System.out.println("Username is empty, triggering validation by clicking Continue.");
            driver.findElement(By.id("continue")).click();
            Thread.sleep(1200);
            return; 
        }

        driver.findElement(By.id("ap_email_login")).sendKeys(username);
        Thread.sleep(800);
        driver.findElement(By.id("continue")).click();
        Thread.sleep(1200);

        if (!driver.findElements(By.id("intent-confirmation-container")).isEmpty()) {
            WebElement newUserBox = driver.findElement(By.id("intent-confirmation-container"));
            System.out.println("Account not found → " + newUserBox.findElement(By.tagName("h1")).getText());
            return;
        }


        if (password != null && !password.isEmpty()) {
            driver.findElement(By.id("ap_password")).sendKeys(password);
            Thread.sleep(800);
            driver.findElement(By.id("signInSubmit")).click();
        } else {
            System.out.println("Password empty - not attempting sign-in.");
        }

        Thread.sleep(1000);
    }



    @Then("I should see an error message")
    public void i_should_see_an_error_message() throws InterruptedException {
        try {
        	if (!driver.findElements(By.id("intent-confirmation-container")).isEmpty()) {
                WebElement newUserBox = driver.findElement(By.id("intent-confirmation-container"));
                System.out.println("Error Message Displayed: No account found → " 
                    + newUserBox.findElement(By.tagName("h1")).getText());
            } 
            else if (!driver.findElements(By.className("a-alert-content")).isEmpty()) {
                WebElement emptyUserErr = driver.findElement(By.className("a-alert-content"));
                System.out.println("Error Message Displayed: Empty Username → " + emptyUserErr.getText());
            }
            else if (!driver.findElements(By.className("a-list-item")).isEmpty()) {
                WebElement err = driver.findElement(By.className("a-list-item"));
                System.out.println("Error Message Displayed: " + err.getText());
            } 
        } catch (Exception e) {
            System.out.println("No error message element found.");
        } finally {
            Thread.sleep(500);
            driver.quit();
        }
    }
    @When("I stay inactive for a long time")
    public void i_stay_inactive_for_a_long_time() throws InterruptedException {
        System.out.println("Simulating inactivity...");

        Thread.sleep(5000); 


        driver.manage().deleteAllCookies();
        System.out.println("Cookies cleared to simulate session expiration.");


        driver.navigate().refresh();
        Thread.sleep(2000);
    }

    @Then("I should be logged out automatically")
    public void i_should_be_logged_out_automatically() throws InterruptedException {
        driver.navigate().refresh();
        if (driver.getCurrentUrl().contains("ap/signin")) {
            System.out.println("Session timeout successful - User is logged out.");
        } else {
            System.out.println("User is still logged in - Session timeout failed.");
        }
        Thread.sleep(1500);
        driver.quit();
    }

    @When("I logout securely")
    public void i_logout_securely() throws InterruptedException {
    	Actions actions = new Actions(driver);


    	WebElement accountMenu = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-link-accountList")));
    	actions.moveToElement(accountMenu).perform();
    	Thread.sleep(1000); 

    	WebElement signOutLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Sign Out']")));
    	signOutLink.click();
    	Thread.sleep(3000);

    	System.out.println("User logged out securely.");
    }

    @Then("I should not access user pages after logout")
    public void i_should_not_access_user_pages_after_logout() throws InterruptedException {
        driver.get("https://www.amazon.in/your-account");
        Thread.sleep(1000);
        if (driver.getCurrentUrl().contains("ap/signin")) {
            System.out.println("Access blocked after logout - Secure logout verified.");
        } else {
            System.out.println("User can still access pages - Secure logout failed.");
        }
        Thread.sleep(500);
        driver.quit();
    }
}

