// LoginSteps.java
package stepDefinition;

import pages.*;
import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;

public class LoginSteps {
    private LoginPage login;
    private DashboardPage dash;

    @Given("I open the Parabank login page")
    public void openLogin() {
        login = new LoginPage(Hooks.driver);
        login.open();
    }

    @When("I login with username {string} and password {string}")
    public void login(String u, String p) throws InterruptedException {
        if (login == null) {
            login = new LoginPage(Hooks.driver);
        }
        login.login(u, p);
        Thread.sleep(2000);
        dash = new DashboardPage(Hooks.driver);
    }

    @Then("I should see dashboard")
    public void seeDashboard() {
        Assertions.assertNotNull(dash, "Dashboard page object not initialized.");
        Assertions.assertTrue(dash.isLoaded(), "Dashboard not loaded after login.");
    }

    @Then("I should see login error")
    public void seeError() {
        Assertions.assertNotNull(login, "Login page object not initialized.");
        String err = login.getError();
        Assertions.assertNotNull(err, "Expected an error message but found null.");
        Assertions.assertFalse(err.trim().isEmpty(), "Error message was empty.");
    }
}
