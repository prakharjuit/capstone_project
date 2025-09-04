// Hooks.java
package stepDefinition;

import utils.BrowserFactory;

import io.cucumber.java.*;
import org.openqa.selenium.*;

public class Hooks {
  public static WebDriver driver;

  @Before
  public void setUp(){
    driver = BrowserFactory.create(System.getProperty("browser","firefox"));
    driver.manage().window().maximize();
  }



  @After
  public void tearDown(){
    if (driver != null) driver.quit();
  }
}
