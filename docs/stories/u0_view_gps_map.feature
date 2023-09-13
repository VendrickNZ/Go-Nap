Feature: I want to be able to see a map of the world around me So that I can define alarms

  Scenario: AC1 - View map
    Given I am on my phone's home screen
    When I open the app
    Then I am shown a map of the world

  Scenario: AC2 - Center map on my live location
    Given I have location services turned on
    When I view the map
    Then The map is centered around my live location

  Scenario: AC3 - Navigate on map with fling interaction
    Given I am viewing the map
    When I swipe in a direction on the map
    Then My view is moved in the direction of the swipe