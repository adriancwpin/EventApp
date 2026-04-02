package eventApp.controller;

import eventApp.enums.BookingStatus;
import eventApp.external.PaymentSystem;
import eventApp.model.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BookPerformanceSystemTests extends SystemInitialisation{

    // SUCCESS CASE
    // First EP must register, login and create the event and performance

    @Test
    @DisplayName("Student successfully books performance")
    void testBookPerformanceSuccess(){
        TestHelper.loginAsEP(userController, view, verificationService);
        Event event = TestHelper.createTestEvent(eventPerformanceController, view);

        // now student can book event
        TestHelper.loginAsStudent(userController, view);
        TestHelper.bookTestPerformance(bookingController, view, paymentSystem);

        // verify booking is successful and active
        Booking booking = bookingController.getBookings().iterator().next();
        assertEquals(BookingStatus.ACTIVE, booking.getStatus());
        verify(view).displaySuccess("Booking Successful");
    }

    // ERROR couldn't book performance because bookingController is null
    // there were 2 bugs, bookings wasn't initialised in SystemInitialisation
    // and BookingController had an infinite loop, it was passing an input to another get input

    // FAILURE CASES

    // CASE 1 performanceID doesn't exist
    @Test
    @DisplayName("Student fails to books performance, performanceID doesn't exist")
    void testBookPerformanceFailure_NoExistingPerformanceID(){
        TestHelper.loginAsEP(userController, view, verificationService);
        Event event = TestHelper.createTestEvent(eventPerformanceController, view);

        // now student can book event
        TestHelper.loginAsStudent(userController, view);
        when(view.getInput("Enter Performance ID (or '-1' to return back to dashboard)" +
                ": ")).thenReturn("100").thenReturn("1");
        when(view.getInput("Enter Number of tickets (or '-1' to return back to dashboard)" +
                ": ")).thenReturn("5");
        when(view.getInput("Press ENTER to return to dashboard...\n")).thenReturn("");
        bookingController.bookPerformance();

        // verify error is caught
        verify(view).displayError("Performance with this ID does not exists.");

        // verify booking is then successful
        Booking booking = bookingController.getBookings().iterator().next();
        assertEquals(BookingStatus.ACTIVE, booking.getStatus());
        verify(view).displaySuccess("Booking Successful");
    }

    // CASE 2 performanceID is an invalid format, NumberFormatException
    @Test
    @DisplayName("Student fails to books performance, invalid performanceID")
    void testBookPerformanceFailure_InvalidPerformanceID(){
        TestHelper.loginAsEP(userController, view, verificationService);
        Event event = TestHelper.createTestEvent(eventPerformanceController, view);

        // now student can book event
        TestHelper.loginAsStudent(userController, view);
        when(view.getInput("Enter Performance ID (or '-1' to return back to dashboard)" +
                ": ")).thenReturn("abc").thenReturn("1");
        when(view.getInput("Enter Number of tickets (or '-1' to return back to dashboard)" +
                ": ")).thenReturn("5");
        when(view.getInput("Press ENTER to return to dashboard...\n")).thenReturn("");
        bookingController.bookPerformance();

        // verify error is caught
        verify(view).displayError("Invalid performance ID. Please enter a valid number.");

        // verify booking is then successful
        Booking booking = bookingController.getBookings().iterator().next();
        assertEquals(BookingStatus.ACTIVE, booking.getStatus());
        verify(view).displaySuccess("Booking Successful");
    }

    // CASE 3 performanceID payment issue
    // must create a scenario where the payment system return false
    @Test
    @DisplayName("Student fails to books performance, payment issue")
    void testBookPerformanceFailure_PaymentIssue(){
        PaymentSystem paymentSystemFailure = mock(PaymentSystem.class);
        when(paymentSystemFailure.processPayment(anyInt(), anyString(), anyString()
                , anyInt(), anyString(), anyDouble())).thenReturn(false);

        bookingController = new BookingController(bookings, performances, view, paymentSystemFailure);

        TestHelper.loginAsEP(userController, view, verificationService);
        Event event = TestHelper.createTestEvent(eventPerformanceController, view);

        // now student can book event
        TestHelper.loginAsStudent(userController, view);
        TestHelper.bookTestPerformance(bookingController, view, paymentSystemFailure);

        // verify error is caught
        Booking booking = bookingController.getBookings().iterator().next();
        assertEquals(BookingStatus.PAYMENTFAILED, booking.getStatus());
        verify(view).displayError("There was an issue with payment.");
    }

    // EARLY RETURN CASE
    @Test
    @DisplayName("Student fails to books performance, performanceID doesn't exist")
    void testBookPerformanceEarlyReturn(){
        TestHelper.loginAsEP(userController, view, verificationService);
        Event event = TestHelper.createTestEvent(eventPerformanceController, view);

        // now student can book event
        TestHelper.loginAsStudent(userController, view);
        when(view.getInput("Enter Performance ID (or '-1' to return back to dashboard)" +
                ": ")).thenReturn("-1");
        when(view.getInput("Enter Number of tickets (or '-1' to return back to dashboard)" +
                ": ")).thenReturn("5");
        bookingController.bookPerformance();

        // verify booking is then not successful
        verify(view, never()).displaySuccess("Booking Successful");
    }
}