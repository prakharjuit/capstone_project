// BrowserFactory.java
package utils;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.firefox.*;

public class BrowserFactory {
  public static WebDriver create(String browser){
    if (browser == null || browser.isBlank() || browser.equalsIgnoreCase("chrome")){
      WebDriverManager.chromedriver().setup();
      return new ChromeDriver();
    }
    if (browser.equalsIgnoreCase("firefox")){
      WebDriverManager.firefoxdriver().setup();
      return new FirefoxDriver();
    }
    throw new IllegalArgumentException("Unsupported browser: " + browser);
  }
}
