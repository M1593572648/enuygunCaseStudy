Feature: Flight search

  Scenario: User searches a flight successfully
    Given user is on the home page
    When user accept to cookies
    And user enters "Istanbul" to from field
    And user enters "Ankara" to destination field
    And user selects departure date "2025-01-15"
    And user selects return date "2025-01-20"
    And user clicks search button
    Then flight results should be displayed
