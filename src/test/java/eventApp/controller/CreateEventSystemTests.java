package eventApp.controller;

import org.junit.jupiter.api.*;
import eventApp.model.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateEventSystemTests extends SystemInitialisation{

    // SUCCESS CASE
    // To create an event first EP must register and login
    @Test
    @DisplayName("Successfully create an event")
    void testSuccessfulEventCreation(){
        TestHelper.loginAsEP(userController, view, verificationService);
        Event event = TestHelper.createTestEvent(eventPerformanceController, view);

        // verify using the values created in createTestEvent
        TestHelper.assertEventCreatedSuccess(event, "Test Concert", true);
        verify(view).displaySuccess("\nEvent 'Test Concert' created successfully! \n");
    }
    // Test is failing because assertNotNull is giving null
    // Solution was to change currentUser to static so it is now shared across all controllers
    // Before it had a different currentUser for each controller, so EP wasn't logged in

    // FAILURE CASE

    // CASE 1 current user is not an EP, i.e. guest, student or admin
    @Test
    @DisplayName("Cannot create event because user is a student")
    void testEventCreationFailureNonEP(){
        TestHelper.loginAsStudent(userController, view);
        Event event = TestHelper.createTestEvent(eventPerformanceController, view);

        assertNull(event, "Event should not be created");
        assertFalse(userController.checkCurrentUserIsEntertainmentProvider());
        verify(view).displayError("Only Entertainment Providers can be create events.");
    }

    // CASE 2 event title is empty
    // Since it is a while loop, must then correctly enter a title
    @Test
    @DisplayName("Cannot create event because title is empty")
    void testEventCreationFailureTitleEmpty(){
        TestHelper.loginAsEP(userController, view, verificationService);

        when(view.getInput("\nEnter Event Title: ")).thenReturn("").thenReturn("Test Concert");
        when(view.getInput("Enter Choice: ")).thenReturn("1");
        when(view.getInput("\nIs this event ticketed? (Yes/No): ")).thenReturn("Yes");
        when(view.getInput("\nNumber of performances: ")).thenReturn("1");
        when(view.getInput("Enter Start Date (dd/MM/yyyy): ")).thenReturn("01/01/3000");
        when(view.getInput("Enter Start Time (HH:mm): ")).thenReturn("00:00");
        when(view.getInput("Enter End Date (dd/MM/yyyy): ")).thenReturn("10/10/3000");
        when(view.getInput("Enter End Time (HH:mm): ")).thenReturn("11:11");
        when(view.getInput("\nEnter Venue Address: ")).thenReturn("Test address 1");
        when(view.getInput("\nEnter Venue Capacity: ")).thenReturn("50");
        when(view.getInput("\nIs the venue outdoor? (Yes/No): ")).thenReturn("Yes");
        when(view.getInput("\nIs smoking allowed? (Yes/No): ")).thenReturn("Yes");
        when(view.getInput("\nNumber of performers: ")).thenReturn("1");
        when(view.getInput("\nEnter Performance 1 name: ")).thenReturn("The Band");
        when(view.getInput("\nNumber of tickets: ")).thenReturn("50");
        when(view.getInput("\nEnter Ticket Price: ")).thenReturn("5");
        when(view.getInput("Press ENTER to return to dashboard...\n")).thenReturn("");

        Event event = eventPerformanceController.createEvent();

        // verify the error is caught
        verify(view).displayError("Event title cannot be empty");

        // verify title is then successfully entered
        // verify using the values created in createTestEvent
        TestHelper.assertEventCreatedSuccess(event, "Test Concert", true);

        verify(view).displaySuccess("\nEvent 'Test Concert' created successfully! \n");
    }

    // CASE 3 invalid input (not yes or no) for isTicketed
    @Test
    @DisplayName("Cannot create event because invalid input for isTicketed")
    void testEventCreationFailureInvalidIsTicketed(){
        TestHelper.loginAsEP(userController, view, verificationService);

        when(view.getInput("\nEnter Event Title: ")).thenReturn("Test Concert");
        when(view.getInput("Enter Choice: ")).thenReturn("1");
        when(view.getInput("\nIs this event ticketed? (Yes/No): "))
                .thenReturn("Invalid").thenReturn("Yes");
        when(view.getInput("\nNumber of performances: ")).thenReturn("1");
        when(view.getInput("Enter Start Date (dd/MM/yyyy): ")).thenReturn("01/01/3000");
        when(view.getInput("Enter Start Time (HH:mm): ")).thenReturn("00:00");
        when(view.getInput("Enter End Date (dd/MM/yyyy): ")).thenReturn("10/10/3000");
        when(view.getInput("Enter End Time (HH:mm): ")).thenReturn("11:11");
        when(view.getInput("\nEnter Venue Address: ")).thenReturn("Test address 1");
        when(view.getInput("\nEnter Venue Capacity: ")).thenReturn("50");
        when(view.getInput("\nIs the venue outdoor? (Yes/No): ")).thenReturn("Yes");
        when(view.getInput("\nIs smoking allowed? (Yes/No): ")).thenReturn("Yes");
        when(view.getInput("\nNumber of performers: ")).thenReturn("1");
        when(view.getInput("\nEnter Performance 1 name: ")).thenReturn("The Band");
        when(view.getInput("\nNumber of tickets: ")).thenReturn("50");
        when(view.getInput("\nEnter Ticket Price: ")).thenReturn("5");
        when(view.getInput("Press ENTER to return to dashboard...\n")).thenReturn("");

        Event event = eventPerformanceController.createEvent();

        // verify the error is caught
        verify(view).displayError("Invalid input. Please enter Yes or No.");

        // verify then successfully entered
        // verify using the values created in createTestEvent
        TestHelper.assertEventCreatedSuccess(event, "Test Concert", true);

        verify(view).displaySuccess("\nEvent 'Test Concert' created successfully! \n");
    }

    // CASE 4 invalid date/time format, this the same for both start and end
    @Test
    @DisplayName("Cannot create event, invalid date/time format")
    void testEventCreationFailureInvalidDateFormat(){
        TestHelper.loginAsEP(userController, view, verificationService);

        when(view.getInput("\nEnter Event Title: ")).thenReturn("Test Concert");
        when(view.getInput("Enter Choice: ")).thenReturn("1");
        when(view.getInput("\nIs this event ticketed? (Yes/No): ")).thenReturn("Yes");
        when(view.getInput("\nNumber of performances: ")).thenReturn("1");
        when(view.getInput("Enter Start Date (dd/MM/yyyy): "))
                .thenReturn("0100/01/20").thenReturn("01/01/3000");
        when(view.getInput("Enter Start Time (HH:mm): ")).thenReturn("00:00");
        when(view.getInput("Enter End Date (dd/MM/yyyy): ")).thenReturn("10/10/3000");
        when(view.getInput("Enter End Time (HH:mm): ")).thenReturn("11:11");
        when(view.getInput("\nEnter Venue Address: ")).thenReturn("Test address 1");
        when(view.getInput("\nEnter Venue Capacity: ")).thenReturn("50");
        when(view.getInput("\nIs the venue outdoor? (Yes/No): ")).thenReturn("Yes");
        when(view.getInput("\nIs smoking allowed? (Yes/No): ")).thenReturn("Yes");
        when(view.getInput("\nNumber of performers: ")).thenReturn("1");
        when(view.getInput("\nEnter Performance 1 name: ")).thenReturn("The Band");
        when(view.getInput("\nNumber of tickets: ")).thenReturn("50");
        when(view.getInput("\nEnter Ticket Price: ")).thenReturn("5");
        when(view.getInput("Press ENTER to return to dashboard...\n")).thenReturn("");

        Event event = eventPerformanceController.createEvent();

        // verify the error is caught
        verify(view).displayError("Invalid date/time format. Please use dd/MM/yyyy HH:mm format.");

        // verify date is then successfully entered
        // verify using the values created in createTestEvent
        TestHelper.assertEventCreatedSuccess(event, "Test Concert", true);

        verify(view).displaySuccess("\nEvent 'Test Concert' created successfully! \n");
    }

    // CASE 5 end time is before start time
    @Test
    @DisplayName("Cannot create event, end time is before start time")
    void testEventCreationFailureEndBeforeStart(){
        TestHelper.loginAsEP(userController, view, verificationService);

        when(view.getInput("\nEnter Event Title: ")).thenReturn("Test Concert");
        when(view.getInput("Enter Choice: ")).thenReturn("1");
        when(view.getInput("\nIs this event ticketed? (Yes/No): ")).thenReturn("Yes");
        when(view.getInput("\nNumber of performances: ")).thenReturn("1");
        when(view.getInput("Enter Start Date (dd/MM/yyyy): ")).thenReturn("01/01/3000");
        when(view.getInput("Enter Start Time (HH:mm): ")).thenReturn("00:00");
        when(view.getInput("Enter End Date (dd/MM/yyyy): "))
                .thenReturn("10/10/2999").thenReturn("10/10/3000");
        when(view.getInput("Enter End Time (HH:mm): ")).thenReturn("11:11");
        when(view.getInput("\nEnter Venue Address: ")).thenReturn("Test address 1");
        when(view.getInput("\nEnter Venue Capacity: ")).thenReturn("50");
        when(view.getInput("\nIs the venue outdoor? (Yes/No): ")).thenReturn("Yes");
        when(view.getInput("\nIs smoking allowed? (Yes/No): ")).thenReturn("Yes");
        when(view.getInput("\nNumber of performers: ")).thenReturn("1");
        when(view.getInput("\nEnter Performance 1 name: ")).thenReturn("The Band");
        when(view.getInput("\nNumber of tickets: ")).thenReturn("50");
        when(view.getInput("\nEnter Ticket Price: ")).thenReturn("5");
        when(view.getInput("Press ENTER to return to dashboard...\n")).thenReturn("");

        Event event = eventPerformanceController.createEvent();

        //verify the error is caught
        verify(view).displayError("End time must be after start time!");

        // verify error is fixed
        // verify using the values created in createTestEvent
        TestHelper.assertEventCreatedSuccess(event, "Test Concert", true);

        verify(view).displaySuccess("\nEvent 'Test Concert' created successfully! \n");
    }

    // CASE 6 event title + time clash with existing event
    // Must create 2 events where in the second one change the time not to clash after failure
    @Test
    @DisplayName("Cannot create event, title and time clash")
    void testEventCreationFailureEventClash(){
        TestHelper.loginAsEP(userController, view, verificationService);
        Event event1 = TestHelper.createTestEvent(eventPerformanceController, view);

        when(view.getInput("\nEnter Event Title: ")).thenReturn("Test Concert");
        when(view.getInput("Enter Choice: ")).thenReturn("1");
        when(view.getInput("\nIs this event ticketed? (Yes/No): ")).thenReturn("Yes");
        when(view.getInput("\nNumber of performances: ")).thenReturn("1");
        when(view.getInput("Enter Start Date (dd/MM/yyyy): "))
                .thenReturn("01/01/3000").thenReturn("01/01/3001");
        when(view.getInput("Enter Start Time (HH:mm): ")).thenReturn("00:00").thenReturn("00:00");
        when(view.getInput("Enter End Date (dd/MM/yyyy): "))
                .thenReturn("10/10/3000").thenReturn("10/10/3001");
        when(view.getInput("Enter End Time (HH:mm): ")).thenReturn("11:11").thenReturn("11:11");
        when(view.getInput("\nEnter Venue Address: ")).thenReturn("Test address 1");
        when(view.getInput("\nEnter Venue Capacity: ")).thenReturn("50");
        when(view.getInput("\nIs the venue outdoor? (Yes/No): ")).thenReturn("Yes");
        when(view.getInput("\nIs smoking allowed? (Yes/No): ")).thenReturn("Yes");
        when(view.getInput("\nNumber of performers: ")).thenReturn("1");
        when(view.getInput("\nEnter Performance 1 name: ")).thenReturn("The Band");
        when(view.getInput("\nNumber of tickets: ")).thenReturn("50");
        when(view.getInput("\nEnter Ticket Price: ")).thenReturn("5");
        when(view.getInput("Press ENTER to return to dashboard...\n")).thenReturn("");

        Event event2 = eventPerformanceController.createEvent();

        //verify the error is caught
        verify(view).displayError("An event with this name has already exists at the same time!");

        // verify error is fixed
        // verify using the values created in createTestEvent
        TestHelper.assertEventCreatedSuccess(event2, "Test Concert", true);

        verify(view, times(2)).displaySuccess("\nEvent 'Test Concert' created successfully! \n");
    }

    // Error that it only wanted the success statement once but got it twice
    // Fix added 'times(2)' this tells it to expect the success statement twice

    // CASE 7 invalid number input
    // the cases where number must be greater than 0
    // using venueCapacity for this test
    @Test
    @DisplayName("Cannot create event,input not greater than 0")
    void testEventCreationFailureCapacityNegative(){
        TestHelper.loginAsEP(userController, view, verificationService);

        when(view.getInput("\nEnter Event Title: ")).thenReturn("Test Concert");
        when(view.getInput("Enter Choice: ")).thenReturn("1");
        when(view.getInput("\nIs this event ticketed? (Yes/No): ")).thenReturn("Yes");
        when(view.getInput("\nNumber of performances: ")).thenReturn("1");
        when(view.getInput("Enter Start Date (dd/MM/yyyy): ")).thenReturn("01/01/3000");
        when(view.getInput("Enter Start Time (HH:mm): ")).thenReturn("00:00");
        when(view.getInput("Enter End Date (dd/MM/yyyy): ")).thenReturn("10/10/3000");
        when(view.getInput("Enter End Time (HH:mm): ")).thenReturn("11:11");
        when(view.getInput("\nEnter Venue Address: ")).thenReturn("Test address 1");
        when(view.getInput("\nEnter Venue Capacity: ")).thenReturn("-50").thenReturn("50");
        when(view.getInput("\nIs the venue outdoor? (Yes/No): ")).thenReturn("Yes");
        when(view.getInput("\nIs smoking allowed? (Yes/No): ")).thenReturn("Yes");
        when(view.getInput("\nNumber of performers: ")).thenReturn("1");
        when(view.getInput("\nEnter Performance 1 name: ")).thenReturn("The Band");
        when(view.getInput("\nNumber of tickets: ")).thenReturn("50");
        when(view.getInput("\nEnter Ticket Price: ")).thenReturn("5");
        when(view.getInput("Press ENTER to return to dashboard...\n")).thenReturn("");

        Event event = eventPerformanceController.createEvent();

        //verify the error is caught
        verify(view).displayError("Capacity must be greater than 0.");

        // verify error is fixed
        // verify using the values created in createTestEvent
        TestHelper.assertEventCreatedSuccess(event, "Test Concert", true);

        verify(view).displaySuccess("\nEvent 'Test Concert' created successfully! \n");
    }

    // CASE 8 num tickets is greater than venue capacity
    @Test
    @DisplayName("Cannot create event, number of tickets is greater than capacity")
    void testEventCreationFailureTicketsGreaterThanCapacity(){
        TestHelper.loginAsEP(userController, view, verificationService);

        when(view.getInput("\nEnter Event Title: ")).thenReturn("Test Concert");
        when(view.getInput("Enter Choice: ")).thenReturn("1");
        when(view.getInput("\nIs this event ticketed? (Yes/No): ")).thenReturn("Yes");
        when(view.getInput("\nNumber of performances: ")).thenReturn("1");
        when(view.getInput("Enter Start Date (dd/MM/yyyy): ")).thenReturn("01/01/3000");
        when(view.getInput("Enter Start Time (HH:mm): ")).thenReturn("00:00");
        when(view.getInput("Enter End Date (dd/MM/yyyy): ")).thenReturn("10/10/3000");
        when(view.getInput("Enter End Time (HH:mm): ")).thenReturn("11:11");
        when(view.getInput("\nEnter Venue Address: ")).thenReturn("Test address 1");
        when(view.getInput("\nEnter Venue Capacity: ")).thenReturn("50");
        when(view.getInput("\nIs the venue outdoor? (Yes/No): ")).thenReturn("Yes");
        when(view.getInput("\nIs smoking allowed? (Yes/No): ")).thenReturn("Yes");
        when(view.getInput("\nNumber of performers: ")).thenReturn("1");
        when(view.getInput("\nEnter Performance 1 name: ")).thenReturn("The Band");
        when(view.getInput("\nNumber of tickets: ")).thenReturn("100").thenReturn("50");
        when(view.getInput("\nEnter Ticket Price: ")).thenReturn("5").thenReturn("5");
        when(view.getInput("Press ENTER to return to dashboard...\n")).thenReturn("");

        Event event = eventPerformanceController.createEvent();

        //verify the error is caught
        verify(view).displayError("Number of ticket must be less than venueCapacity.");

        // verify error is fixed
        // verify using the values created in createTestEvent
        TestHelper.assertEventCreatedSuccess(event, "Test Concert", true);

        verify(view).displaySuccess("\nEvent 'Test Concert' created successfully! \n");
    }

    // Error infinite loop
    // Fix in EventPerformanceController add to the while condition to check for
    // when numTickets > venueCapacity

    // CASE 9 when there is a numberFormatException
    // will test on numPerformers
    @Test
    @DisplayName("Cannot create event, numberFormatException for numPerformers")
    void testEventCreationFailureNumberFormatException(){
        TestHelper.loginAsEP(userController, view, verificationService);

        when(view.getInput("\nEnter Event Title: ")).thenReturn("Test Concert");
        when(view.getInput("Enter Choice: ")).thenReturn("1");
        when(view.getInput("\nIs this event ticketed? (Yes/No): ")).thenReturn("Yes");
        when(view.getInput("\nNumber of performances: ")).thenReturn("1");
        when(view.getInput("Enter Start Date (dd/MM/yyyy): ")).thenReturn("01/01/3000");
        when(view.getInput("Enter Start Time (HH:mm): ")).thenReturn("00:00");
        when(view.getInput("Enter End Date (dd/MM/yyyy): ")).thenReturn("10/10/3000");
        when(view.getInput("Enter End Time (HH:mm): ")).thenReturn("11:11");
        when(view.getInput("\nEnter Venue Address: ")).thenReturn("Test address 1");
        when(view.getInput("\nEnter Venue Capacity: ")).thenReturn("50");
        when(view.getInput("\nIs the venue outdoor? (Yes/No): ")).thenReturn("Yes");
        when(view.getInput("\nIs smoking allowed? (Yes/No): ")).thenReturn("Yes");
        when(view.getInput("\nNumber of performers: ")).thenReturn("abc").thenReturn("1");
        when(view.getInput("\nEnter Performance 1 name: ")).thenReturn("The Band");
        when(view.getInput("\nNumber of tickets: ")).thenReturn("50");
        when(view.getInput("\nEnter Ticket Price: ")).thenReturn("5");
        when(view.getInput("Press ENTER to return to dashboard...\n")).thenReturn("");

        Event event = eventPerformanceController.createEvent();

        //verify the error is caught
        verify(view).displayError("Invalid input. Please enter a valid number.");

        // verify error is fixed
        // verify using the values created in createTestEvent
        TestHelper.assertEventCreatedSuccess(event, "Test Concert", true);

        verify(view).displaySuccess("\nEvent 'Test Concert' created successfully! \n");
    }

    // CASE 10 performance time clashes
    @Test
    @DisplayName("Cannot create event, performance times clash")
    void testEventCreationFailurePerformanceClashes(){
        TestHelper.loginAsEP(userController, view, verificationService);

        // format is performance 1, performance 2, then performance 2 again to fix the error
        // if the input doesn't change there is only 1 thenReturn
        when(view.getInput("\nEnter Event Title: ")).thenReturn("Test Concert");
        when(view.getInput("Enter Choice: ")).thenReturn("1");
        when(view.getInput("\nIs this event ticketed? (Yes/No): ")).thenReturn("Yes");
        when(view.getInput("\nNumber of performances: ")).thenReturn("2");
        when(view.getInput("Enter Start Date (dd/MM/yyyy): "))
                .thenReturn("01/01/3000")
                .thenReturn("01/01/3000")
                .thenReturn("01/01/3001");
        when(view.getInput("Enter Start Time (HH:mm): ")).thenReturn("00:00");
        when(view.getInput("Enter End Date (dd/MM/yyyy): "))
                .thenReturn("10/10/3000")
                .thenReturn("10/10/3000")
                .thenReturn("10/10/3001");
        when(view.getInput("Enter End Time (HH:mm): ")).thenReturn("11:11");
        when(view.getInput("\nEnter Venue Address: ")).thenReturn("Test address 1");
        when(view.getInput("\nEnter Venue Capacity: ")).thenReturn("50");
        when(view.getInput("\nIs the venue outdoor? (Yes/No): ")).thenReturn("Yes");
        when(view.getInput("\nIs smoking allowed? (Yes/No): ")).thenReturn("Yes");
        when(view.getInput("\nNumber of performers: ")).thenReturn("1");
        when(view.getInput("\nEnter Performance 1 name: ")).thenReturn("The Band");
        when(view.getInput("\nEnter Performance 2 name: ")).thenReturn("Another Band");
        when(view.getInput("\nNumber of tickets: ")).thenReturn("50");
        when(view.getInput("\nEnter Ticket Price: ")).thenReturn("5");
        when(view.getInput("Press ENTER to return to dashboard...\n")).thenReturn("");

        Event event = eventPerformanceController.createEvent();

        //verify the error is caught
        verify(view).displayError("A performance already exists at this time");

        // verify error is fixed
        // verify using the values created in createTestEvent
        TestHelper.assertEventCreatedSuccess(event, "Test Concert", true);

        verify(view).displaySuccess("\nEvent 'Test Concert' created successfully! \n");
    }
}

