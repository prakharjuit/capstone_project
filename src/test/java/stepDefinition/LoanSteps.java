package stepDefinition;

import pages.DashboardPage;
import pages.RequestLoanPage;
import io.cucumber.java.en.*;
import static org.junit.jupiter.api.Assertions.*;

public class LoanSteps {
  DashboardPage dash;
  RequestLoanPage loan;

  @Given("I am logging in")
  public void iAmLoggedIn() throws InterruptedException{
    LoginSteps ls = new LoginSteps();
    ls.openLogin();
    ls.login("userXYZ","Password123");   // use known demo creds or parametrize via env
    dash = new DashboardPage(Hooks.driver);
    assertTrue(dash.isLoaded());
  }

  @When("I request a loan of {string} with down payment {string} from my first account")
  public void requestLoan(String amount, String down) throws InterruptedException{
    dash.openRequestLoan();
    Thread.sleep(1000);
    loan = new RequestLoanPage(Hooks.driver);
    loan.apply(amount, down);
    Thread.sleep(1000);
  }

  @Then("I should see a loan decision displayed")
  public void loanDecision() throws InterruptedException{
    Thread.sleep(1000);

    // Basic title presence
    String title = loan.getOutcomeTitle();
    System.out.println("Outcome title: " + title);
    assertNotNull(title, "Expected a visible outcome title");

    // Status cell
    String status = loan.getStatus();
    System.out.println("Loan status: " + status);

    // Branching assertions depending on status
    if (status != null && status.equalsIgnoreCase("Approved")) {
      String newAcc = loan.getNewAccountId();
      System.out.println("Approved: new account id = " + newAcc);
      assertNotNull(newAcc, "Approval should produce a new account id");
      assertFalse(newAcc.trim().isEmpty(), "New account id should not be empty");
    } else if (status != null && status.equalsIgnoreCase("Denied")) {
      String reason = loan.getDeniedMessage();
      System.out.println("Denied reason: " + reason);
      assertNotNull(reason, "Denied flow should display a reason");
      assertFalse(reason.trim().isEmpty(), "Denied reason should not be empty");
    } else {
      // unknown but still ensure outcome contains something meaningful
      String provider = loan.getProviderName();
      String date = loan.getResponseDate();
      System.out.println("Provider: " + provider + ", Date: " + date + ", rawStatus: " + status);
      // pass if title contains expected phrase
      assertTrue(title.toLowerCase().contains("loan request") || title.toLowerCase().contains("processed"),
          "Unexpected loan outcome: " + title);
    }
  }
}
