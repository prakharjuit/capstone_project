package stepDefinition;

import pages.RegistrationPage;
import io.cucumber.java.en.*;
import org.openqa.selenium.By;

import static org.junit.jupiter.api.Assertions.*;

public class RegistrationSteps {

    RegistrationPage reg;

    @When("I open the registration page")
    public void i_open_registration_page() {
        reg = new RegistrationPage(Hooks.driver);
        reg.open();
    }

    @When("I register with valid details")
    public void i_register_with_valid_details() throws InterruptedException {

        
        reg.fillBasic("John", "Doe", "123 Main St", "City", "State", "12345", "999-999-9999",
                      "123-45-6789", "userrr", "Password123", "Password123");
        Thread.sleep(2000);
        reg.clickRegister();
        Thread.sleep(2000);
    }

    @Then("I should see a registration success message")
    public void should_see_success_message() throws InterruptedException {
        String txt = reg.getSuccessText();

        assertNotNull(txt, "Expected success title text");
        assertTrue(txt.toLowerCase().contains("welcome") || txt.toLowerCase().contains("account created")
                   || txt.toLowerCase().contains("welcome"), "Unexpected success text: " + txt);
        Thread.sleep(2000);
    }

    @When("I submit the registration form with missing mandatory fields")
    public void submit_missing_mandatory_fields() throws InterruptedException {

        String uniqueUser = ""; 
        reg.fillBasic("", "Doe", "123 Main", "City", "State", "12345", "999-999-9999",
                      "123-45-6789", uniqueUser, "Password123", "Password123");
        Thread.sleep(2000);
        reg.clickRegister();
        Thread.sleep(2000);
    }

    @Then("I should see validation errors for required fields")
    public void should_see_required_errors() throws InterruptedException {
    	
       
        assertTrue(reg.isFieldEmpty(reg.getFirstNameLocator()), "First name should be empty / flagged");
        Thread.sleep(2000);
        assertTrue(reg.isFieldEmpty(reg.getUsernameLocator()), "Username should be empty / flagged");
        Thread.sleep(2000);
    }

   
}
