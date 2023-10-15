# Go Nap
Michael Alpenfels, Jakib Isherwood

## Important notes for cloning

This app depends on Google Maps API for map data. You will need to add your API key to the `MAPS_API_KEY`
property of`local.properties`.

## Grade Checklist


Grade bearing requirements met: 
1. ~~Interact with with the nearby physical world in some way.~~ We implemented Location/GPS tracking
2. **Interact with with the nearby physical world in some additional way.**
3. ~~Handle one type of input based on a non-simple (i.e. non-click) Gesture in a
   non-standard way, e.g. fling, drag, multitouch, etc.~~ Swipeable list item to reveal delete button
4. **Provide a facility for "openness" (Plan: backup alarms)**
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
15. **Create a user test for your app (Test with Priscilla and Henry)**
16. ~~Incorporate some other Android feature not mentioned above~~ Google Maps SDK
17. ~~Incorporate some other Android feature not mentioned above~~ Geofencing
18. ~~Provide at least four weekly reports~~ (3/4)
19. ~~Successfully demo of your app during the last week of class~~ :)