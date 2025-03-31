# Employee Directory
The project created as part of the take home test for Block/Square.

## Build tools & versions used
- Android Studio Meerkat | 2024.3.1 Patch 1
- Android Gradle Plugin (AGP) 8.9.1
- Android 15.0 (API Level 35)
- Android SDK Build-Tools 35.0.0

## Steps to run the app
No changes should be needed to run the app.  The default run configuration should work.

## What areas of the app did you focus on?
I focused on code simplicity, readability, and maintainability, while also striving to provide a simple, straightforward user experience.

## What was the reason for your focus? What problems were you trying to solve?
With my core focus on code simplicity, readability, and maintainability, the goal was to make sure that the codebase would be easy to improve and expand on, while also being quick to understand by those unfamiliar with it.

Meanwhile, the focus on the user experience ensured the app would be easy to pick up for a new user, and that they would be able to easily discover the different pieces of functionality of the app (e.g., sorting and refreshing).

## How long did you spend on this project?
I spent approximately 7-8 hours in total on this project.

## Did you make any trade-offs for this project? What would you have done differently with more time?
One of the more notable tradeoffs I made, functionality-wise, was using a menu button for the refresh functionality, rather than using `SwipeRefreshLayout`.  The reasons for this were:
1. I had issues getting `SwipeRefreshLayout` to properly handle the newest edge-to-edge behavior introduced with Android 15.0, so I figured I should go with a simpler, more straightforward solution for now
2. I wanted to ensure the app took into account various potential user accessibility needs, which a menu option better addresses (and which is pointed out in https://developer.android.com/develop/ui/views/touch-and-input/swipe/add-swipe-interface)

With more time, I would probably provide both options for users, as pull-to-refresh is a very natural user action, while the menu option ensures accessibility for a greater number of users.

Other things I would consider with more time:
- Add unit tests for EmployeesRepositoryTest, especially if more complex logic is added to it.
- Introduce Timber, or other logging library, to help with debugging and log reporting to various logging services.
- Try out a collapsing toolbar when scrolling to see if that makes for a better user experience.
- Provide a way to sort by either first or last name.  With only a single "full_name" field currently returned in the JSON, though, this might require a few more complex requirements considerations to ensure we properly handle scenarios where we receive single-word names (e.g., "Madonna"), or 3+-word names (e.g., "Leonardo da Vinci").
- Provide a way to sort in ascending or descending order.
- Provide a search filter to quickly narrow down the list by name, team, or email.
- Add email and phone number to the list since that would likely be helpful in a directory app.  Clicking them could also automatically launch an email or phone app for even easier use.
- Add the ability to click on the employee photo to show the large, higher-resolution image that is already returned.
- Add the ability to click on a list item to see an overlay containing a more detailed view of the employee, which could include the biography.
- Although it was explicitly stated that we do not want to persist the employee data, we could provide an in-memory caching solution with something such as an `EmployeesLocalDataSource` class.
- When no employees are returned from the back-end, we could hide the "Sort" menu option since there's nothing to sort.
- Instead of exposing one of the `AllEmployeesViewModel` `MutableStateFlow`s to outside classes, I would consider finding a way to inject the initial state specifically for unit test usage.
- Potentially introduce `MaterialCardView` to add more visual flair to the list items.
- Add line count limits name and team if we foresee scenarios where those fields will sometimes be very long.

## What do you think is the weakest part of your project?
I would say there is a lot of room for improvement with the style and colors of the app, especially when looking across both light and dark themes.  That said, I did take the time to ensure that all text was readable and that icons were visible in both themes.

## Did you copy any code or dependencies? Please make sure to attribute them here!
I referenced quite a bit of Android documentation, and I leveraged Android Studio's Gemini integration for some advice here and there, but I did not copy any large chunks of code into this codebase.

## Is there any other information you’d like us to know?
- I went with a `ListAdapter` to back the employee list since that gives us a lot of basic functionality out of the box, particularly the animated reordering and updating of the list as the data is changed, while reducing boilerplate code and providing performance efficiencies.