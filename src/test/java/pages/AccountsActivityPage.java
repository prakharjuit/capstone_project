// AccountsActivityPage.java
package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;

public class AccountsActivityPage {
  private final WebDriver driver;
  private final WebDriverWait wait;

  private final By accountLinkById(String id){ return By.linkText(id); }
  private final By transactionsTable = By.id("transactionTable");

  public AccountsActivityPage(WebDriver d){ driver = d; wait = new WebDriverWait(d, Duration.ofSeconds(10)); }

  public void openAccount(String id){
    driver.findElement(accountLinkById(id)).click();
    wait.until(ExpectedConditions.visibilityOfElementLocated(transactionsTable));
  }

  public boolean hasTransactionWithAmount(String amount){
    return driver.findElement(transactionsTable).getText().contains(amount);
  }
}
