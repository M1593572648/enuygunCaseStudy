Feature: Flight search



  Scenario Outline: Basic Flight Search and Time Filter
    Given user is on the home page
    When user accepts cookies
    And user click round trip
    And user enters "<fromCity>" to from field
    And user enters "<toCity>" to destination field
    And user clicks on the departure date field
    And user selects the departure date "<departureDate>"
    And user clicks the return date field
    And user selects the return date "<returnDate>"
    And user clicks search cheap flight button
    And user wait reloading page
    And user clicks on the departure and return dates field
    And user selects flight time range with start <startTimeRange> and end <endTimeRange>
    Then all list displayed and verify the departure time between 10.00 and 18.00
    Examples:
      | fromCity  | toCity    | departureDate | returnDate | startTimeRange | endTimeRange |
      | Istanbul  | Ankara    | 2026-01-01    | 2026-01-10 |    600         |    1080       |
      #| Ankara    | Istanbul |

  Scenario Outline:Price Sorting for Turkish Airlines
    Given user is on the home page
    When user accepts cookies
    And user click round trip
    And user enters "<fromCity>" to from field
    And user enters "<toCity>" to destination field
    And user clicks on the departure date field
    And user selects the departure date "<departureDate>"
    And user clicks the return date field
    And user selects the return date "<returnDate>"
    And user clicks search cheap flight button
    And user wait reloading page
    And user clicks on the departure and return dates field
    And user selects flight time range with start <startTimeRange> and end <endTimeRange>
    And user choose only turkish airlines
    Then is ascending price and selected thy
    Examples:
      | fromCity  | toCity    | departureDate | returnDate | startTimeRange | endTimeRange |
      | Istanbul  | Ankara    | 2026-01-01    | 2026-01-10 |    600         |    1080       |
      #| Ankara    | Istanbul |

  Scenario Outline:Time filter and Price Sorting for Turkish Airlines and Select flight
    Given user is on the home page
    When user accepts cookies
    And user click round trip
    And user enters "<fromCity>" to from field
    And user enters "<toCity>" to destination field
    And user clicks on the departure date field
    And user selects the departure date "<departureDate>"
    And user clicks the return date field
    And user selects the return date "<returnDate>"
    And user clicks search cheap flight button
    And user wait reloading page
    And user clicks on the departure and return dates field
    And user selects flight time range with start <startTimeRange> and end <endTimeRange>
    And user choose only turkish airlines
    And user select first button in the div
    Then is url changed
    Examples:
      | fromCity  | toCity    | departureDate | returnDate | startTimeRange | endTimeRange |
      | Istanbul  | Ankara    | 2026-01-01    | 2026-01-10 |    600         |    1080       |
      #| Ankara    | Istanbul |


  Scenario Outline:Analysis and Categorization
    Given user is on the home page
    When user accepts cookies
    And user click round trip
    And user enters "<fromCity>" to from field
    And user enters "<toCity>" to destination field
    And user clicks on the departure date field
    And user selects the departure date "<departureDate>"
    And user clicks the return date field
    And user selects the return date "<returnDate>"
    And user clicks search cheap flight button
    And user wait reloading page
    And user clicks on the departure and return dates field
    And user selects flight time range with start <startTimeRange> and end <endTimeRange>
    Then flight list is extracted to CSV
    And CSV data is analyzed and graphs are generated

    Examples:
      | fromCity | toCity   | departureDate | returnDate | startTimeRange | endTimeRange |
      | Istanbul | Lefkoşa  | 2026-01-01    | 2026-01-10 | 600            | 1080         |


