Feature: Fund Transfer
  Scenario: Transfer funds and verify balances & history
    Given I am logged in
    And I am opening transfer funds page
    When I transfer "10" from the first account to the second account
    Then the transfer should be successful

