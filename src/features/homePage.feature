Feature: Flight search



  Scenario Outline: Temel uçuş arama ve saat filtresi
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
    And user clicks on the departure and return dates field
    And user selects flight time range with start <startTimeRange> and end <endTimeRange>

    Examples:
      | fromCity  | toCity    | departureDate | returnDate | startTimeRange | endTimeRange |
      | Istanbul  | Ankara    | 2026-01-01    | 2026-01-10 |    10          |    18        |
      #| Ankara    | Istanbul |
