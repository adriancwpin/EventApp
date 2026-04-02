package eventApp.controller;

import eventApp.enums.BookingStatus;
import eventApp.enums.PerformanceStatus;
import eventApp.external.PaymentSystem;
import eventApp.model.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CancelPerformanceSystemTests extends SystemInitialisation{

    // SUCCESS CASES
    // only EPs are allowed to cancel performances
    // First an event and a performance must be created
    // In this scenario it doesn't differ if there is or isn't bookings

    // SUCCESS CASE 1 no bookings
    @Test
    @DisplayName("EP successfully cancels performance with no bookings")
    void testEPCancelPerformanceSuccess_NoBookings(){
        TestHelper.loginAsEP(userController, view, verificationService);
        Event event = TestHelper.createTestEvent(eventPerformanceController, view);

        // EP can now cancel performance
        TestHelper.loginAsEP(userController, view, verificationService);
        when(view.getInput("Enter ID of performance to cancel (or '-1' to return)" +
                ": ")).thenReturn("1");
        when(view.getInput("Provide a cancellation message for affected students" +
                ": ")).thenReturn("Apologies event was cancelled");
        when(view.getInput("Press ENTER to return to dashboard... \n")).thenReturn("");

        eventPerformanceController.cancelPerformance();

        // verify successful cancellation
        Performance performance = eventPerformanceController.getPerformances().iterator().next();
        assertEquals(PerformanceStatus.CANCELLED, performance.getStatus());

        verify(view).displaySuccess("Cancellation Successful.");
    }

    // ERROR date was in the past so checkHasNotHappenedYet was failing
    // FIX change performance dates to the future

    // SUCCESS CASE 2 performance has bookings
    @Test
    @DisplayName("EP successfully cancels performance with bookings")
    void testEPCancelPerformanceSuccess_HasBookings(){
        TestHelper.loginAsEP(userController, view, verificationService);
        Event event = TestHelper.createTestEvent(eventPerformanceController, view);

        TestHelper.loginAsStudent(userController, view);
        TestHelper.bookTestPerformance(bookingController, view, paymentSystem);

        // EP can now cancel performance
        TestHelper.loginAsEP(userController, view, verificationService);
        when(view.getInput("Enter ID of performance to cancel (or '-1' to return)" +
                ": ")).thenReturn("1");
        when(view.getInput("Provide a cancellation message for affected students" +
                ": ")).thenReturn("Apologies event was cancelled");
        when(view.getInput("Press ENTER to return to dashboard... \n")).thenReturn("");

        eventPerformanceController.cancelPerformance();

        // verify successful cancellation
        Performance performance = eventPerformanceController.getPerformances().iterator().next();
        assertEquals(PerformanceStatus.CANCELLED, performance.getStatus());

        Booking booking = bookingController.getBookings().iterator().next();
        assertEquals(BookingStatus.CANCELLEDBYPROVIDER, booking.getStatus());

        verify(view).displaySuccess("Cancellation Successful.");
    }

    // FAILURE CASES
    // CASE 1 currentUser is not an EP
    @Test
    @DisplayName("Non EP user cannot cancel performance")
    void testNonEPCancelPerformanceFailure(){
        TestHelper.loginAsEP(userController, view, verificationService);
        Event event = TestHelper.createTestEvent(eventPerformanceController, view);

        TestHelper.loginAsStudent(userController, view);

        eventPerformanceController.cancelPerformance();

        // verify error caught and performance is not cancelled
        Performance performance = eventPerformanceController.getPerformances().iterator().next();
        assertEquals(PerformanceStatus.ACTIVE, performance.getStatus());
        assertFalse(userController.checkCurrentUserIsEntertainmentProvider());

        verify(view).displayError("Only Entertainment Provider can cancel performance.");
    }

    // CASE 2 performance with given ID doesn't exist
    @Test
    @DisplayName("EP cannot cancel performance, performance doesn't exist")
    void testEPCancelPerformanceFailure_PerformanceNotFound(){
        TestHelper.loginAsEP(userController, view, verificationService);
        Event event = TestHelper.createTestEvent(eventPerformanceController, view);

        TestHelper.loginAsEP(userController, view, verificationService);
        when(view.getInput("Enter ID of performance to cancel (or '-1' to return): "))
                .thenReturn("100").thenReturn("-1");

        eventPerformanceController.cancelPerformance();

        // verify error caught and performance is not cancelled
        Performance performance = eventPerformanceController.getPerformances().iterator().next();
        assertEquals(PerformanceStatus.ACTIVE, performance.getStatus());

        verify(view).displayError("Performance with given number does not exists.");
    }

    // CASE 3 performance is created by a different EP
    @Test
    @DisplayName("EP cannot cancel performance, performance belongs to different EP")
    void testEPCancelPerformanceFailure_PerformanceIsDifferentEPs(){
        TestHelper.loginAsEP(userController, view, verificationService);
        Event event = TestHelper.createTestEvent(eventPerformanceController, view);

        // now log in as a different EP
        TestHelper.registerTestEP(userController, view, verificationService,
                "Test org 2", "BN99999999", "Doe",
                "We organise more tests", "newerep@test.com", "testep999");
        when(view.getInput("Enter email: ")).thenReturn("newerep@test.com");
        when(view.getInput("Enter password: ")).thenReturn("testep999");
        userController.login();

        when(view.getInput("Enter ID of performance to cancel (or '-1' to return): "))
                .thenReturn("1").thenReturn("-1");

        eventPerformanceController.cancelPerformance();

        // verify error caught and performance is not cancelled
        Performance performance = eventPerformanceController.getPerformances().iterator().next();
        assertEquals(PerformanceStatus.ACTIVE, performance.getStatus());

        verify(view).displayError("The performance with given number does not belong to you.");
    }

    // CASE 4 event has already happened
    @Test
    @DisplayName("EP cannot cancel performance, performance has already happened")
    void testEPCancelPerformanceFailure_PerformanceAlreadyHappened(){
        TestHelper.loginAsEP(userController, view, verificationService);

        // must create the event in a past date
        when(view.getInput("\nEnter Event Title: ")).thenReturn("Test Concert");
        when(view.getInput("Enter Choice: ")).thenReturn("1");
        when(view.getInput("\nIs this event ticketed? (Yes/No): ")).thenReturn("Yes");
        when(view.getInput("\nNumber of performances: ")).thenReturn("1");
        when(view.getInput("Enter Start Date (dd/MM/yyyy): ")).thenReturn("01/01/2000");
        when(view.getInput("Enter Start Time (HH:mm): ")).thenReturn("00:00");
        when(view.getInput("Enter End Date (dd/MM/yyyy): ")).thenReturn("10/10/2000");
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
        eventPerformanceController.createEvent();

        TestHelper.loginAsEP(userController, view, verificationService);
        when(view.getInput("Enter ID of performance to cancel (or '-1' to return): "))
                .thenReturn("1").thenReturn("-1");

        eventPerformanceController.cancelPerformance();

        // verify error caught and performance is not cancelled
        Performance performance = eventPerformanceController.getPerformances().iterator().next();
        assertEquals(PerformanceStatus.ACTIVE, performance.getStatus());

        verify(view).displayError("Performance cannot be cancelled as it has already happened.");
    }

    // CASE 5 invalid PerformanceID
    @Test
    @DisplayName("EP cannot cancel performance, performanceID is invalid")
    void testEPCancelPerformanceFailure_PerformanceIDInvalid(){
        TestHelper.loginAsEP(userController, view, verificationService);
        Event event = TestHelper.createTestEvent(eventPerformanceController, view);

        TestHelper.loginAsEP(userController, view, verificationService);
        when(view.getInput("Enter ID of performance to cancel (or '-1' to return): "))
                .thenReturn("abc").thenReturn("-1");

        eventPerformanceController.cancelPerformance();

        // verify error caught and performance is not cancelled
        Performance performance = eventPerformanceController.getPerformances().iterator().next();
        assertEquals(PerformanceStatus.ACTIVE, performance.getStatus());

        verify(view).displayError("Invalid ID, please enter a number.");
    }

    // CASE 6 no cancellation message provided
    @Test
    @DisplayName("EP cannot cancel performance, no cancellation message provided")
    void testEPCancelPerformanceFailure_EmptyCancellationMessage(){
        TestHelper.loginAsEP(userController, view, verificationService);
        Event event = TestHelper.createTestEvent(eventPerformanceController, view);

        TestHelper.loginAsEP(userController, view, verificationService);
        when(view.getInput("Enter ID of performance to cancel (or '-1' to return)" +
                ": ")).thenReturn("1");
        when(view.getInput("Provide a cancellation message for affected students" +
                ": ")).thenReturn("").thenReturn("Apologies event was cancelled");
        when(view.getInput("Press ENTER to return to dashboard... \n")).thenReturn("");

        eventPerformanceController.cancelPerformance();

        // verify error was caught
        verify(view).displayError("Provide a cancellation message for affected students.");

        // verify successful cancellation
        Performance performance = eventPerformanceController.getPerformances().iterator().next();
        assertEquals(PerformanceStatus.CANCELLED, performance.getStatus());

        verify(view).displaySuccess("Cancellation Successful.");
    }

    // CASE 7 there is an issue with refund
    // must create a scenario where the payment system return false
    @Test
    @DisplayName("EP cannot cancel performance, refund issue")
    void testEPCancelPerformanceFailure_RefundIssue(){
        PaymentSystem paymentSystemFailure = mock(PaymentSystem.class);
        when(paymentSystemFailure.processPayment(anyInt(), anyString(), anyString()
                , anyInt(), anyString(), anyDouble())).thenReturn(false);

        eventPerformanceController = new EventPerformanceController(events, performances,
                view, paymentSystemFailure);

        TestHelper.loginAsEP(userController, view, verificationService);
        Event event = TestHelper.createTestEvent(eventPerformanceController, view);

        // get a student to book a ticket
        TestHelper.loginAsStudent(userController, view);
        TestHelper.bookTestPerformance(bookingController, view, paymentSystemFailure);

        TestHelper.loginAsEP(userController, view, verificationService);
        when(view.getInput("Enter ID of performance to cancel (or '-1' to return)" +
                ": ")).thenReturn("1");
        when(view.getInput("Provide a cancellation message for affected students" +
                ": ")).thenReturn("Apologies event was cancelled");

        eventPerformanceController.cancelPerformance();

        // verify error caught and performance is not cancelled
        Performance performance = eventPerformanceController.getPerformances().iterator().next();
        assertEquals(PerformanceStatus.ACTIVE, performance.getStatus());

        verify(view).displayError("There was an issue with a refund." +
                " The performance cannot be cancelled.");
    }

    // EARLY RETURN CASE EP enters -1 to return/exit
    @Test
    @DisplayName("EP enter -1 to return/exit")
    void testEPCancelPerformanceExit(){
        TestHelper.loginAsEP(userController, view, verificationService);
        Event event = TestHelper.createTestEvent(eventPerformanceController, view);

        // EP can now cancel performance
        TestHelper.loginAsEP(userController, view, verificationService);
        when(view.getInput("Enter ID of performance to cancel (or '-1' to return)" +
                ": ")).thenReturn("-1");

        eventPerformanceController.cancelPerformance();

        // verify error caught and performance is not cancelled
        Performance performance = eventPerformanceController.getPerformances().iterator().next();
        assertEquals(PerformanceStatus.ACTIVE, performance.getStatus());

        verify(view, never()).displaySuccess("Cancellation Successful.");
    }
}