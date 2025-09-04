// LoginPage.java
package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;

public class LoginPage {
  private final WebDriver driver;
  private final WebDriverWait wait;
  private final By username = By.name("username");
  private final By password = By.name("password");
  private final By loginBtn = By.cssSelector("input.button");
  private final By errorMsg = By.cssSelector("#rightPanel .error, #rightPanel .title + p");

  public LoginPage(WebDriver driver){
    this.driver = driver;
    this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
  }

  public void open(){
    driver.get("https://parabank.parasoft.com/parabank/index.htm");
    wait.until(ExpectedConditions.visibilityOfElementLocated(username));
  }

  public void login(String user, String pass){
    driver.findElement(username).clear();
    driver.findElement(username).sendKeys(user);
    driver.findElement(password).clear();
    driver.findElement(password).sendKeys(pass);
    driver.findElement(loginBtn).click();
  }

  public String getError(){
    try {
      return driver.findElement(errorMsg).getText().trim();
    } catch (NoSuchElementException e){ return null; }
  }
}
