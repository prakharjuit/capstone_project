package stepDefinition;
import pages.*;
import io.cucumber.java.en.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class TransferSteps {
  DashboardPage dash;
  TransferFundPage tf;

  @Given("I am logged in")
  public void loggedInDashboard() throws InterruptedException {
    LoginSteps ls = new LoginSteps();
    ls.openLogin();
    ls.login("Prakhar2024", "Prakhar123@");
    dash = new DashboardPage(Hooks.driver);
    assertTrue(dash.isLoaded(), "Dashboard not loaded");
  }

  @And("I am opening transfer funds page")
  public void openTransfer() throws InterruptedException {
    dash.openTransferFunds();
    tf = new TransferFundPage(Hooks.driver);
    Thread.sleep(1000); // small pause to let selects render
    List<String> from = tf.getFromAccountValues();
    List<String> to = tf.getToAccountValues();
    assertFalse(from.isEmpty(), "No 'from' accounts found");
    assertFalse(to.isEmpty(), "No 'to' accounts found");
  }
  @When("I transfer {string} from the first account to the second account")
  public void doTransfer(String amount) throws InterruptedException {
    tf.transfer(amount, "0", "1");

  }

  @Then("the transfer should be successful")
  public void transferSuccess() {
      assertTrue(tf.isSuccess(), "Transfer header not visible");

      // Optional stronger check:
      TransferFundPage.TransferResult res = tf.getTransferResult();
      assertNotNull(res, "Transfer details missing");
      System.out.println("Transfer details: " + res.amount + " from " + res.fromAccount + " to " + res.toAccount);
  }

}
