Feature: As a user, I want to be able to define an alarm at a location on the map So that I can take a
  nap on public transport and wake up before I miss my stop

  Scenario: AC1 - Choose alarm location
    Given I am on the app map page
    When I tap on a location on the map
    Then I am shown a dialog to create an alarm

  Scenario: AC2 - Confirm alarm location
    Given I am viewing a dialog to create an alarm
    When I press the confirm alarm button
    Then The alarm is created with a pre-defined radius and saved

  Scenario: AC3 - Define alarm name
    Given I am viewing a dialog to create an alarm
    When I enter an alarm name
    And I press the confirm alarm button
    Then The alarm is created with my set name and saved

  Scenario: AC4 - Define alarm sound
    Given I am viewing a dialog to create an alarm
    When I enter an alarm sound
    And I press the confirm alarm button
    Then The alarm is created with my set sound and saved

  Scenario: AC5 - View alarm on map
    Given I have defined an alarm at a location
    When I view that location on the map
    Then I can see a circle outlining the alarm with the radius equal to the alarm radius

  Scenario: AC6 - View the alarm
    Given I am looking at an alarm radius on the map
    When I press anywhere in the radius
    Then I am shown a box with the details of the alarm


