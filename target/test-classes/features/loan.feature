Feature: Loan Application & Decisions
  Scenario: Apply for Personal Loan with valid details
    Given I am logging in
    When I request a loan of "1000" with down payment "100" from my first account
    Then I should see a loan decision displayed

