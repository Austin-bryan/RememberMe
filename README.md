\# RememberMe!


\## About the app



RememberMe! is a mobile calendar and event tracking application designed to help users quickly create, manage, and review events tied to specific calendar days. The app focuses on a clean, user-centered workflow where users select a day first, then create or manage events within that context. The goal was to build a functional, database-backed mobile app that demonstrates real CRUD operations, user authentication, and basic notification permissions while maintaining a simple and intuitive UI.



\## Questions



\### Briefly summarize the requirements and goals of the app you developed. What user needs was this app designed to address?



The app was designed to provide a simple and efficient way for users to manage events tied to specific calendar days. The goal was to implement core mobile app features such as authentication, database CRUD operations, and user interaction through a clean and intuitive interface. The app addresses the need for quick event creation and easy navigation between days and events.



\### What screens and features were necessary to support user needs and produce a user-centered UI for the app? How did your UI designs keep users in mind? Why were your designs successful?



The app required a login screen, calendar view, day view, event view, and a create/edit event screen. The UI was designed to minimize redundant input, especially by removing the need to manually enter dates. Users interact with the app through a natural flow starting from the calendar, which keeps the experience intuitive. The designs were successful because they reduced user error and kept interactions consistent across screens.



\### How did you approach the process of coding your app? What techniques or strategies did you use? How could those techniques or strategies be applied in the future?



The app was built incrementally, starting with UI and gradually adding functionality. I used intent-based navigation to pass data between screens and relied on the database as the single source of truth. Lifecycle methods like onResume() were used to refresh data automatically. These strategies can be applied in future projects to maintain clean architecture and reduce bugs related to state management.



\### How did you test to ensure your code was functional? Why is this process important, and what did it reveal?



Testing was done using the Android Emulator by walking through all major flows, including login, event creation, editing, deletion, and navigation. This process is important because it reveals real-world issues that are not obvious from reading code alone. It revealed a major flaw in date handling, which was fixed by restructuring how dates are passed and stored.



\### Consider the full app design and development process from initial planning to finalization. Where did you have to innovate to overcome a challenge?



The biggest area of innovation was fixing the date handling system. Instead of relying on user input, the app uses the selected calendar date as the authoritative value. This eliminated a class of bugs and simplified the user experience.



\### In what specific component of your mobile app were you particularly successful in demonstrating your knowledge, skills, and experience?



The event lifecycle system, including create, update, delete, and automatic UI refresh, demonstrates strong understanding of mobile app architecture. The use of database-driven views combined with lifecycle-aware updates shows the ability to build reliable and maintainable application behavior.



\## Limitations



Not every feature from the original design could be implemented due to time constraints and overlapping deadlines. As a result, the core requirements to meet the project were added and tested, but if with more time, these are what would have been added:



\### Color Selection for Events



Event color customization was not implemented. All events use a default color to keep the focus on core functionality such as CRUD operations and database integration.



\### Event Confirmation State



The “confirmed vs not confirmed” feature was designed but not implemented. All events are treated as confirmed to reduce complexity and prioritize required features.



\### Recurring Events



Recurring events were not implemented due to the additional complexity of scheduling logic and database modeling. The app currently supports single-instance events only.



\### Advanced Notification Scheduling



While SMS permission handling is implemented, full scheduling of notifications based on event time was not completed. Instead, the app demonstrates permission handling and test messaging functionality.



\### Month Navigation Logic



The calendar view uses a simplified structure with partial hardcoding for adjacent months. Full dynamic month navigation was not implemented, as it was outside the core requirements.

