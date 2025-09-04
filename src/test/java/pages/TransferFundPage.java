package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class TransferFundPage {
  private final WebDriver driver;
  private final WebDriverWait wait;

  private final By amount = By.id("amount");
  private final By fromAccount = By.id("fromAccountId");
  private final By toAccount = By.id("toAccountId");
  private final By transferBtn = By.cssSelector("input.button"); // works on ParaBank
  private final By successMsg = By.cssSelector("#rightPanel h1");

  public TransferFundPage(WebDriver d) {
    this.driver = d;
    this.wait = new WebDriverWait(d, Duration.ofSeconds(10));
  }

  private List<String> optionValues(By selectLocator) {
    WebElement sel = wait.until(ExpectedConditions.visibilityOfElementLocated(selectLocator));
    return new Select(sel).getOptions()
        .stream().map(opt -> opt.getAttribute("value")).collect(Collectors.toList());
  }

  public List<String> getFromAccountValues() { return optionValues(fromAccount); }
  public List<String> getToAccountValues()   { return optionValues(toAccount);   }

  public void transfer(String amt, String fromValOrIndex, String toValOrIndex) throws InterruptedException {
	    WebElement amtField = wait.until(ExpectedConditions.elementToBeClickable(amount));
	    amtField.clear();
	    amtField.sendKeys(amt);

	    // helper to select safely
	    selectOptionSafely(fromAccount, fromValOrIndex);
	    selectOptionSafely(toAccount, toValOrIndex);
	    Thread.sleep(2000);
	    driver.findElement(transferBtn).click();
	    Thread.sleep(2000);
	}

	private void selectOptionSafely(By selectLocator, String valueOrIndexOrText) {
	    WebElement selectEl = wait.until(ExpectedConditions.visibilityOfElementLocated(selectLocator));

	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", selectEl);

	    // log options for debugging
	    Select sel = new Select(selectEl);
	    List<WebElement> options = sel.getOptions();

	    try {
	        if (selectEl.isDisplayed() && selectEl.isEnabled()) {
	            new Actions(driver).moveToElement(selectEl).click().perform();
	            // small pause so UI can open if needed
	            Thread.sleep(200);
	        }
	    } catch (InterruptedException | WebDriverException ignored) {
	        // not critical — selection will still work via Select
	    }

	    // attempt select by value
	    try {
	        sel.selectByValue(valueOrIndexOrText);
	        System.out.println("Selected by value: " + valueOrIndexOrText);
	        return;
	    } catch (NoSuchElementException ignored) {
	        // fallback below
	    }

	    // fallback: if input is an integer, treat as index
	    try {
	        int idx = Integer.parseInt(valueOrIndexOrText);
	        if (idx < 0) idx = 0;
	        if (idx >= options.size()) idx = options.size() - 1;
	        sel.selectByIndex(idx);
	        System.out.println("Selected by index: " + idx);
	        return;
	    } catch (NumberFormatException ignored) {
	        // not an index, try visible text
	    }

	    try {
	        sel.selectByVisibleText(valueOrIndexOrText);
	        System.out.println("Selected by visible text: " + valueOrIndexOrText);
	        return;
	    } catch (NoSuchElementException e) {

	        if (!options.isEmpty()) {
	            sel.selectByIndex(0);
	            System.out.println("Fallback: selected first option index 0");
	        } else {
	            throw new NoSuchElementException("No options available for select: " + selectLocator);
	        }
	    }
	}

	public boolean isSuccess() {
	    try {

	        WebElement result = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("showResult")));
	        WebElement h1 = result.findElement(By.cssSelector("h1.title"));
	        if (h1 == null || !h1.getText().toLowerCase().contains("transfer complete")) {
	            return false;
	        }
	        WebElement amt = result.findElement(By.id("amountResult"));
	        WebElement from = result.findElement(By.id("fromAccountIdResult"));
	        WebElement to = result.findElement(By.id("toAccountIdResult"));


	        if (amt == null || amt.getText().trim().isEmpty()) return false;
	        if (from == null || from.getText().trim().isEmpty()) return false;
	        if (to == null || to.getText().trim().isEmpty()) return false;

	        System.out.printf("Transfer success: %s from #%s to #%s%n",
	                amt.getText().trim(), from.getText().trim(), to.getText().trim());

	        return true;
	    } catch (TimeoutException | NoSuchElementException e) {
	        System.out.println("Transfer success block not visible or missing elements: " + e.getMessage());
	        return false;
	    }
	}

	public TransferResult getTransferResult() {
	    try {
	        WebElement result = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("showResult")));
	        String amountText = result.findElement(By.id("amountResult")).getText().trim();
	        String from = result.findElement(By.id("fromAccountIdResult")).getText().trim();
	        String to = result.findElement(By.id("toAccountIdResult")).getText().trim();
	        return new TransferResult(amountText, from, to);
	    } catch (Exception e) {
	        return null;
	    }
	}


	public static class TransferResult {
	    public final String amount;
	    public final String fromAccount;
	    public final String toAccount;
	    public TransferResult(String amount, String fromAccount, String toAccount) {
	        this.amount = amount;
	        this.fromAccount = fromAccount;
	        this.toAccount = toAccount;
	    }
	}

}
