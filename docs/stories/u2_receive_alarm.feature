Feature: As a user, I want to be able to receive notifications when I am located in an alarm area
  So that I can be woken up from a nap

  Scenario: AC1 - Receive Alarms
    Given I have set an alarm
    And I am not in an alarm's area of notification
    When I enter the alarm's area of notification
    Then I receive a loud notification to wake me up

  Scenario: AC2 - Stop alarms
    Given An alarm is currently activated
    When I press the stop button
    Then The the alarm is silenced and not activated again until I exit the area
