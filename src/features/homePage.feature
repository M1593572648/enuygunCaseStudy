Feature: Flight search



  Scenario Outline: Temel uçuş arama ve saat filtresi
    Given user is on the home page
    When user accepts cookies
    And user click round trip
    And user enters "<fromCity>" to from field
    And user enters "<toCity>" to destination field

    Examples:
      | fromCity | toCity   |
      | Istanbul  | Ankara   |
      #| Ankara    | Istanbul |
