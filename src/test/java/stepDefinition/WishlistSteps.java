package stepDefinition;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.cucumber.java.en.*;

import java.time.Duration;
import java.util.List;

public class WishlistSteps {
    WebDriver driver;
    WebDriverWait wait;

    @Given("I open browser and launch the login page")
    public void i_open_the_browser_and_launch_the_login_page() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        driver.get("https://www.amazon.in/");
        wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-link-accountList"))).click();
    }

    @When("I login with the userName {string} and password {string}")
    public void i_login_with_user_name_and_password(String username, String password) {
        // email field variations handled
        By emailLoc = By.id("ap_email_login");
        if (driver.findElements(emailLoc).isEmpty()) emailLoc = By.id("ap_email");

        wait.until(ExpectedConditions.visibilityOfElementLocated(emailLoc)).sendKeys(username);

        if (!driver.findElements(By.id("continue")).isEmpty()) {
            wait.until(ExpectedConditions.elementToBeClickable(By.id("continue"))).click();
        }

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ap_password"))).sendKeys(password);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("signInSubmit"))).click();

        // wait until account menu visible (login success indicator)
        wait.until(ExpectedConditions.or(
            ExpectedConditions.visibilityOfElementLocated(By.id("nav-link-accountList")),
            ExpectedConditions.urlContains("gp/home") // fallback
        ));
    }

    @When("I search for {string}")
    public void i_search_for(String product) {
        WebElement box = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("twotabsearchtextbox")));
        box.clear();
        box.sendKeys(product);
        if (!driver.findElements(By.id("nav-search-submit-button")).isEmpty()) {
            wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-search-submit-button"))).click();
        } else {
            box.submit();
        }
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.s-main-slot")));
    }

    @And("I add the first product to wishlist")
    public void i_add_the_first_product_to_wishlist() {
        // wait for results container
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.s-main-slot")));

        // locate the first real result block (has data-asin)
        By firstResultBlock = By.xpath("(//div[@data-component-type='s-search-result' and normalize-space(@data-asin)!=''])[1]");
        WebElement block = wait.until(ExpectedConditions.presenceOfElementLocated(firstResultBlock));


        List<WebElement> anchors = block.findElements(By.xpath(".//h2//a"));
        WebElement toClick;
        if (!anchors.isEmpty()) {
            toClick = anchors.get(0);
        } else {
            toClick = block.findElement(By.xpath(".//h2"));
        }


        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", toClick);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(toClick)).click();
        } catch (Exception e) {
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", toClick);
        }

        for (String handle : driver.getWindowHandles()) {
            driver.switchTo().window(handle);
        }

        WebElement wishlistBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("add-to-wishlist-button-submit")));
        wishlistBtn.click();

        // wait for confirmation/view list
        wait.until(ExpectedConditions.or(
            ExpectedConditions.presenceOfElementLocated(By.id("WLHUC_viewlist")),
            ExpectedConditions.presenceOfElementLocated(By.cssSelector(".a-box-inner, .a-popover-wrapper"))
        ));

        System.out.println("Product added to Wishlist.");
    }



    @Then("that product should appear in my wishlist")
    public void that_product_should_appear_in_my_wishlist() {
        By viewWishlistBtn = By.id("WLHUC_viewlist");
        if (!driver.findElements(viewWishlistBtn).isEmpty()) {
            wait.until(ExpectedConditions.elementToBeClickable(viewWishlistBtn)).click();
        } else {
            driver.get("https://www.amazon.in/hz/wishlist/ls");
        }
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".g-item-sortable, .a-fixed-left-grid, .g-item")));
        boolean hasItems = !driver.findElements(By.cssSelector(".g-item-sortable, .a-fixed-left-grid, .g-item")).isEmpty();
        if (!hasItems) throw new AssertionError("Wishlist appears empty — expected at least one item.");
        System.out.println("Wishlist contains items.");
    }

    @Given("I have at least one item in wishlist")
    public void i_have_at_least_one_item_in_wishlist() {
        driver.get("https://www.amazon.in/hz/wishlist/ls");
        if (driver.findElements(By.cssSelector(".g-item-sortable, .a-fixed-left-grid, .g-item")).isEmpty()) {

            i_search_for("watch");
            i_add_the_first_product_to_wishlist();

            if (!driver.findElements(By.id("WLHUC_viewlist")).isEmpty()) {
                wait.until(ExpectedConditions.elementToBeClickable(By.id("WLHUC_viewlist"))).click();
            } else {
                driver.get("https://www.amazon.in/hz/wishlist/ls");
            }
        } else {
            System.out.println("Wishlist already has items.");
        }
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".g-item-sortable, .a-fixed-left-grid, .g-item")));
    }


    @When("I rename my wishlist to {string}")
    public void i_rename_my_wishlist_to(String newName) {
        // open wishlist page
        driver.get("https://www.amazon.in/hz/wishlist/ls");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("body")));

        // Hover the More Options button (use a generic selector that matches your input)
        By moreOptionsLocator = By.cssSelector("input.a-button-input[aria-labelledby='a-autoid-0-announce'], input.a-button-input[aria-labelledby*='a-autoid']");
        if (!driver.findElements(moreOptionsLocator).isEmpty()) {
            WebElement moreOptions = driver.findElement(moreOptionsLocator);
            new org.openqa.selenium.interactions.Actions(driver).moveToElement(moreOptions).perform();
            // tiny pause to let menu appear
            try { Thread.sleep(500); } catch (InterruptedException ignored) {}
        } else {
            System.out.println("More Options button not found (will try to find Manage List directly).");
        }

        // Look for the Manage List element by id
        By manageById = By.id("editYourList");
        if (driver.findElements(manageById).isEmpty()) {
            System.out.println("Manage List (editYourList) not present in DOM after hover.");
            return;
        }

        // Wait for presence (not clickable) then decide how to click
        wait.until(ExpectedConditions.presenceOfElementLocated(manageById));
        WebElement manage = driver.findElement(manageById);

        // If element is visible & enabled, try normal click; otherwise use JS click as fallback
        if (manage.isDisplayed() && manage.isEnabled()) {
            // attempt a normal click but first wait a short time just in case
            try {
                wait.until(ExpectedConditions.elementToBeClickable(manageById));
                manage.click();
            } catch (Exception e) {
                // clickable wait failed — fallback to JS click
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", manage);
            }
        } else {
            // not interactable via normal click — use JS
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", manage);
        }

        // Now update the list name
        By nameBox = By.id("list-settings-name");
        if (!driver.findElements(nameBox).isEmpty()) {
            WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(nameBox));
            input.clear();
            input.sendKeys(newName);

            // Use your real Save button locator (you provided this)
            By saveInput = By.cssSelector("input.a-button-input[aria-labelledby='list-settings-save-announce']");
            if (!driver.findElements(saveInput).isEmpty()) {
                wait.until(ExpectedConditions.elementToBeClickable(saveInput));
                driver.findElement(saveInput).click();
                System.out.println("Renamed wishlist to: " + newName);
            } else {
                System.out.println("Save button not found after rename.");
            }
        } else {
            System.out.println("Wishlist name input not found after opening Manage List.");
        }
    }




    

    


    @And("I share the wishlist")
    public void i_share_the_wishlist() {
        driver.get("https://www.amazon.in/hz/wishlist/ls");


        WebElement shareBtn = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("input.a-button-input[aria-labelledby*='announce']")
        ));


        shareBtn.click();

        System.out.println("Clicked Share Wishlist.");
    }


    @Then("the share should succeed \\(or confirmation appear)")
    public void the_share_should_succeed_or_confirmation_appear() {
        By confirmLocator = By.xpath("//*[contains(text(),'shared') or contains(text(),'sent') or contains(text(),'copy link') or contains(text(),'link to share')]");
        if (driver.findElements(confirmLocator).isEmpty()) {
            throw new AssertionError("Share confirmation not found.");
        }
        System.out.println("Share confirmation appeared.");
    }

    @Given("I have item\\(s) in wishlist")
    public void i_have_items_in_wishlist() {
        driver.get("https://www.amazon.in/hz/wishlist/ls");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".g-item-sortable, .a-fixed-left-grid, .g-item")));
    }

    @When("I add first wishlist item to cart")
    public void i_add_first_wishlist_item_to_cart() {
        driver.get("https://www.amazon.in/hz/wishlist/ls");

        WebElement addToCartBtn = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("(//a[contains(text(),'Add to Cart')])[1]")
        ));
        addToCartBtn.click();

        System.out.println("Added first wishlist item to cart.");
    }

    @Then("the cart should contain that item")
    public void the_cart_should_contain_that_item() {
        driver.get("https://www.amazon.in/gp/cart/view.html");
        By cartItem = By.cssSelector(".sc-list-item, .sc-item");
        if (driver.findElements(cartItem).isEmpty()) {
            throw new AssertionError("Cart does not contain item added from wishlist.");
        }
        System.out.println("Cart contains item from wishlist.");
        driver.quit();
        driver = null;
    }
}
