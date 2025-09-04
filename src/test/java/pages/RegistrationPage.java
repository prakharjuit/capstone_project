package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;

public class RegistrationPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public RegistrationPage(WebDriver driver){
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // locators (Parabank registration form)
    private final By firstName = By.id("customer.firstName");
    private final By lastName = By.id("customer.lastName");
    private final By address = By.id("customer.address.street");
    private final By city = By.id("customer.address.city");
    private final By state = By.id("customer.address.state");
    private final By zipCode = By.id("customer.address.zipCode");
    private final By phone = By.id("customer.phoneNumber");
    private final By ssn = By.id("customer.ssn");
    private final By username = By.id("customer.username");
    private final By password = By.id("customer.password");
    private final By confirm = By.id("repeatedPassword");
    private final By registerBtn = By.cssSelector("input.button[value='Register']");
    private final By clearBtn = By.cssSelector("input.button[value='Clear']"); // reset
    private final By successMessage = By.cssSelector("#rightPanel .title");
    private final By form = By.id("customerForm");

    public void open() {
        driver.get("https://parabank.parasoft.com/parabank/register.htm");
        wait.until(ExpectedConditions.visibilityOfElementLocated(form));
    }

    public void fillBasic(String fName, String lName, String addr, String cty,
                          String st, String zip, String ph, String ssnVal,
                          String user, String pwd, String pwdConfirm) {
        driver.findElement(firstName).clear(); driver.findElement(firstName).sendKeys(fName);
        driver.findElement(lastName).clear(); driver.findElement(lastName).sendKeys(lName);
        driver.findElement(address).clear(); driver.findElement(address).sendKeys(addr);
        driver.findElement(city).clear(); driver.findElement(city).sendKeys(cty);
        driver.findElement(state).clear(); driver.findElement(state).sendKeys(st);
        driver.findElement(zipCode).clear(); driver.findElement(zipCode).sendKeys(zip);
        driver.findElement(phone).clear(); driver.findElement(phone).sendKeys(ph);
        driver.findElement(ssn).clear(); driver.findElement(ssn).sendKeys(ssnVal);
        driver.findElement(username).clear(); driver.findElement(username).sendKeys(user);
        driver.findElement(password).clear(); driver.findElement(password).sendKeys(pwd);
        driver.findElement(confirm).clear(); driver.findElement(confirm).sendKeys(pwdConfirm);
    }

    public void clickRegister() {
        driver.findElement(registerBtn).click();
    }

    public void clickClear() {
        driver.findElement(clearBtn).click();
    }

    public String getSuccessText() {
        try {
            return driver.findElement(successMessage).getText().trim();
        } catch (Exception e) {
            return null;
        }
    }

    // small helpers to check field content
    public String getFieldValue(By locator) {
        try { return driver.findElement(locator).getAttribute("value"); }
        catch (Exception e) { return ""; }
    }

    public boolean isFieldEmpty(By locator) {
        return getFieldValue(locator) == null || getFieldValue(locator).trim().isEmpty();
    }

    // exps for tests to use
    public By getFirstNameLocator() { return firstName; }
    public By getLastNameLocator() { return lastName; }
    public By getZipLocator() { return zipCode; }
    public By getUsernameLocator() { return username; }
}
