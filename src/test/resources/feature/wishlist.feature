Feature: Wishlist functionality

  Scenario: Verify products added to wishlist are displayed
    Given I open browser and launch the login page
    And I login with the userName "prakhar011101@gmail.com" and password "Prakhar123@"
    When I search for "laptop"
    And I add the first product to wishlist
    Then that product should appear in my wishlist

  Scenario: Validate update and share wishlist functionality
    Given I open browser and launch the login page
    And I login with the userName "prakhar011101@gmail.com" and password "Prakhar123@"
    And I have at least one item in wishlist
    When I rename my wishlist to "monitor"
    And I share the wishlist
    Then the share should succeed (or confirmation appear)

  Scenario: Ensure items can be added to cart directly from wishlist
    Given I open browser and launch the login page
    And I login with the userName "prakhar011101@gmail.com" and password "Prakhar123@"
    And I have item(s) in wishlist
    When I add first wishlist item to cart
    Then the cart should contain that item
