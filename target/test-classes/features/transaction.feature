Feature: Transaction History
  Scenario: Verify transactions recorded
    Given I am logged in
    When I open my first account activity
    Then I should see at least one transaction entry
