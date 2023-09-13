Feature: I want to be able to define an alarm at a location on the map So that I can take a
  nap on public transport and wake up before I miss my stop

  Scenario: AC1 - Choose alarm location
    Given I am on the app map page
    When I tap on a location on the map
    Then I am shown a dialog to create an alarm

  Scenario: AC2 - Confirm alarm location
    Given I am viewing a dialog to create an alarm
    When I press the confirm location button
    Then The alarm is created with a pre-defined radius and saved

  Scenario: AC3 - View alarm on map
    Given I have defined an alarm at a location
    When I view that location on the map
    Then I can see a circle outlining the alarm with the radius equal to the alarm
    radius
