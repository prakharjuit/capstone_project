Feature: Login

  Scenario Outline: Validate login
    Given I open the Parabank login page
    When I login with username "<username>" and password "<password>"
    Then I should see dashboard

    Examples:
      | username     | password     |
      | Prakharrr| Prakhar123@  |

  Scenario Outline: Invalid login
    Given I open the Parabank login page
    When I login with username "<username>" and password "<password>"
    Then I should see login error

    Examples:
      | username   | password |
      |       | wrong    |
