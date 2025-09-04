// DashboardPage.java
package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;
import java.util.List;

public class DashboardPage {
  private final WebDriver driver;
  private final WebDriverWait wait;

  private final By accountTableRows = By.cssSelector("#accountTable tbody tr");
  private final By accountLink = By.cssSelector("#accountTable tbody tr td a");
  private final By balanceCell = By.cssSelector("#accountTable tbody tr td:nth-child(2)");

  private final By transferFundsLink = By.linkText("Transfer Funds");
  private final By requestLoanLink = By.linkText("Request Loan");
  private final By logoutLink = By.linkText("Log Out");

  public DashboardPage(WebDriver driver){
    this.driver = driver;
    this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
  }

  public boolean isLoaded(){
    return !driver.findElements(accountTableRows).isEmpty();
  }

  public String firstAccountId(){
    return driver.findElements(accountLink).get(0).getText().trim();
  }

  public double firstAccountBalance(){
    String raw = driver.findElements(balanceCell).get(0).getText().replace("$","").replace(",","").trim();
    return Double.parseDouble(raw);
  }

  public void openTransferFunds(){ driver.findElement(transferFundsLink).click(); }
  public void openRequestLoan(){ driver.findElement(requestLoanLink).click(); }
  public void logout(){ driver.findElement(logoutLink).click(); }
}
