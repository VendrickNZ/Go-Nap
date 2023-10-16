# Go Nap
Michael Alpenfels, Jakib Isherwood

## Important notes for cloning

This app depends on Google Maps API for map data. You will need to add your API key to the `MAPS_API_KEY`
property of`local.properties`.

## How to use the app 

Press and hold on the place on the map you want to create an alarm. 

You can specify a custom name and radius per alarm, or set a default in settings.

You can press on any alarm on the map to set it as active. Active alarms are red. 

Entering the radius of the active alarm will then send an alarm notification to you to wake up, and deactivate
the alarm.

From the My Alarms page, you can press any alarm to see where it is on the map. Swiping left on an alarm
in the list will show a delete button. You can also press the share icon to share the alarm to your 
friends in any messaging app. This will create a link that they can click that deep links to GoNap's 
create alarm screen, and will prepopulate the create alarm with the data from the link.

### Important Note for deep linking

Because we do not actually own the domain `www.gonap.nz`, deep linking will need to be manually connected
by the user in their android settings. This can be done by going `Apps > Go Nap > Defaults | Set as default > Supported web addresses` 
then enable `www.gonap.nz`. You should also make sure that `Open supported links` is enabled.

## Grade Checklist

Grade bearing requirements met: 
1. ~~Interact with with the nearby physical world in some way.~~ We implemented Location/GPS tracking
2. ~~Interact with with the nearby physical world in some additional way.~~ See bearing on map from sensor
3. ~~Handle one type of input based on a non-simple (i.e. non-click) Gesture in a
   non-standard way, e.g. fling, drag, multitouch, etc.~~ Swipeable list item to reveal delete button
4. ~~Provide a facility for "openness"~~ Share alarms with a deep link
5. ~~Gracefully handle configuration changes, not losing any of the user's data.~~
6. ~~Use coroutines to trigger some computation without blocking the user interface.~~ We update location on a coroutine
7. ~~Use a local database to persist data~~ Alarms and settings persisted with Room
8. ~~Send the user notifications related to your app in some way.~~ Alarms
9. ~~Integrate an action bar in at least one activity.~~ Done
10. ~~Provide a preference screen.~~
11. ~~Add a multi-resolution launcher icon~~
12. ~~Support both landscape and portrait orientations in all views~~
13. ~~Use string resources for all static text on the user interface~~
14. ~~Provide a sketch of your app’s functionality~~ Done
15. ~~Create a user test for your app (Test with Priscilla and Henry)~~
16. ~~Incorporate some other Android feature not mentioned above~~ Google Maps SDK
17. ~~Incorporate some other Android feature not mentioned above~~ Geofencing
18. ~~Provide at least four weekly reports~~ (3/4)
19. ~~Successfully demo of your app during the last week of class~~ :)

## User test feedback

Priscilla: Hard to figure out how to create alarms

Vincent:
* Hard to figure out how to create alarms
* User Location Icon is small
* images for alarm circles
* different colour scheme