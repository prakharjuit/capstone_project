Feature: Login functionality
Scenario: Successful login with valid credentials
 Given I launch the browser
When I open the login page
And I enter valid UserName "<userName>" and password "<password>"
And I click on login button
Then I should be navigated to the home page
Examples: Successful Login
	|userName |password|
	|userrr        |Password123        |
	
Scenario: Failed login
  Given I open the browser and launch the login page
  When I login with userName "<userName>" and password "<password>"
  Then I should see an error message
Examples: Failed login
	|userName  |password|
	|                     |	|
	|prakhar0112101@gmail.com     |Prakhar123@     |
