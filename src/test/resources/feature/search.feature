Feature: Search functionality

  Background:
    Given I launch the browser
    And I open the home page

  Scenario: Validate relevance of search results
    When I search for "wireless headphones"
    Then first page search results should be relevant to "wireless headphones"

  Scenario: Ensure product details are visible in results
    When I search for "wireless headphones"
    Then each result should show image, name, price and reviews

  Scenario: Verify pagination has no duplicates across pages
    When I search for "wireless headphones"
    And I collect SKUs from first page
    And I navigate to page 2
    Then SKUs on page 2 should not intersect with page 1

  Scenario: Validate sorting and filtering persistence
    When I search for "laptop"
    And I apply filter "Brand: Acer" and sort by "Price: Low to High"
    And I open a product and go back to results
    Then filter "Brand: Acer" and sorting "Price: Low to High" should still be applied

  Scenario: Ensure product counts match filtered criteria
    When I search for "phone"
    And I apply filter "Price: 10000-20000"
    Then the displayed product count should match number of visible results
