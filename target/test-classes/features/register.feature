Feature: User registration
  As a new user
  I want to register an account on Parabank
  So that I can use banking features

  Background:
    Given I open the Parabank login page

  Scenario: Successful registration with valid inputs
    When I open the registration page
    And I register with valid details
    Then I should see a registration success message

  Scenario: Required fields validation
    When I open the registration page
    And I submit the registration form with missing mandatory fields
    Then I should see validation errors for required fields