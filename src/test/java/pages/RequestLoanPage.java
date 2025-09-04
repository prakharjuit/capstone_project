package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;

public class RequestLoanPage {
  private final WebDriver driver;
  private final WebDriverWait wait;

  private final By loanAmount = By.id("amount");
  private final By downPayment = By.id("downPayment");
  private final By fromAccount = By.id("fromAccountId");
  private final By applyBtn = By.cssSelector("input.button");

  // Results
  private final By resultPanel = By.id("requestLoanResult");
  private final By resultTitle = By.cssSelector("#requestLoanResult h1.title");
  private final By loanStatus = By.id("loanStatus");
  private final By loanDeniedMessage = By.cssSelector("#loanRequestDenied .error");
  private final By newAccountLink = By.id("newAccountId");
  private final By providerName = By.id("loanProviderName");
  private final By responseDate = By.id("responseDate");

  public RequestLoanPage(WebDriver d){
    driver = d;
    wait = new WebDriverWait(d, Duration.ofSeconds(10));
  }


  public void apply(String amount, String down){
    wait.until(ExpectedConditions.visibilityOfElementLocated(loanAmount)).clear();
    driver.findElement(loanAmount).sendKeys(amount);

    WebElement dp = driver.findElement(downPayment);
    dp.clear();
    dp.sendKeys(down);

    wait.until(ExpectedConditions.elementToBeClickable(applyBtn)).click();

    // wait for result panel to appear (either approved or denied)
    wait.until(ExpectedConditions.visibilityOfElementLocated(resultPanel));
  }

  public String getOutcomeTitle(){
    try {
      return wait.until(ExpectedConditions.visibilityOfElementLocated(resultTitle)).getText().trim();
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * Returns text in the status cell (e.g. "Approved" or "Denied"), or null.
   */
  public String getStatus(){
    try {
      return driver.findElement(loanStatus).getText().trim();
    } catch (NoSuchElementException e) {
      return null;
    }
  }

  /**
   * If approved, newAccountId link contains the account number. Returns null when missing.
   */
  public String getNewAccountId(){
    try {
      String idText = driver.findElement(newAccountLink).getText().trim();
      return idText.isEmpty() ? null : idText;
    } catch (NoSuchElementException e) {
      return null;
    }
  }

  /**
   * If denied, returns the denial reason text (from #loanRequestDenied .error)
   */
  public String getDeniedMessage(){
    try {
      return driver.findElement(loanDeniedMessage).getText().trim();
    } catch (NoSuchElementException e) {
      return null;
    }
  }

  public String getProviderName(){
    try {
      return driver.findElement(providerName).getText().trim();
    } catch (NoSuchElementException e) {
      return null;
    }
  }

  public String getResponseDate(){
    try {
      return driver.findElement(responseDate).getText().trim();
    } catch (NoSuchElementException e) {
      return null;
    }
  }
}
