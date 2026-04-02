package eventApp.controller;

import eventApp.enums.BookingStatus;
import eventApp.external.PaymentSystem;
import eventApp.model.*;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CancelBookingSystemTests extends SystemInitialisation{

    // To cancel a booking the EP must first register/login and create an event
    // then student must then book a performance to be able to cancel it

    // SUCCESS CASE
    @Test
    @DisplayName("Student successfully cancels booking")
    void testStudentCancelsBookingSuccess(){
        // EP registers and creates event
        TestHelper.loginAsEP(userController, view, verificationService);
        Event event = TestHelper.createTestEvent(eventPerformanceController, view);

        // student logins and books performance
        TestHelper.loginAsStudent(userController, view);
        TestHelper.bookTestPerformance(bookingController, view, paymentSystem);

        // can now begin cancelling booking
        when(view.getInput("Enter Booking Number (or '-1' to return" +
                " back to dashboard: ")).thenReturn("1");
        when(view.getInput("Press ENTER to return to dashboard... \n")).thenReturn("");

        bookingController.cancelBooking();

        // verify successful cancellation
        Booking booking = bookingController.getBookings().iterator().next();
        assertEquals(BookingStatus.CANCELLEDBYSTUDENT, booking.getStatus());

        verify(view).displaySuccess("Booking cancelled successfully.");
    }
    // ERROR infinite loop, same as a previous error.
    // view.getInput was being used on an existing input causing the loop

    // FAILURE CASES
    // CASE 1 user is not a student
    @Test
    @DisplayName("Non Student fails to cancel booking")
    void testNonStudentCancelsBookingFailure(){
        // EP registers and creates event
        TestHelper.loginAsEP(userController, view, verificationService);
        Event event = TestHelper.createTestEvent(eventPerformanceController, view);

        // student logins and books performance
        TestHelper.loginAsStudent(userController, view);
        TestHelper.bookTestPerformance(bookingController, view, paymentSystem);

        TestHelper.loginAsAdmin(userController, view);

        bookingController.cancelBooking();

        // verify error is caught and booking is still active
        Booking booking = bookingController.getBookings().iterator().next();
        assertEquals(BookingStatus.ACTIVE, booking.getStatus());
        assertFalse(userController.checkCurrentUserIsStudent());

        verify(view).displayError("Only student can cancel booking.");
    }

    // CASE 2 booking number does not exist
    @Test
    @DisplayName("Student fails to cancel booking, booking number doesn't exist")
    void testStudentCancelsBookingFailure_BookingNumberDoesntExist(){
        // EP registers and creates event
        TestHelper.loginAsEP(userController, view, verificationService);
        Event event = TestHelper.createTestEvent(eventPerformanceController, view);

        // student logins and books performance
        TestHelper.loginAsStudent(userController, view);
        TestHelper.bookTestPerformance(bookingController, view, paymentSystem);

        when(view.getInput("Enter Booking Number (or '-1' to return" +
                " back to dashboard: ")).thenReturn("100").thenReturn("1");
        when(view.getInput("Press ENTER to return to dashboard... \n")).thenReturn("");

        bookingController.cancelBooking();

        // verify error is caught
        verify(view).displayError("This booking number does not exists.");

        // verify successful cancellation after fix
        Booking booking = bookingController.getBookings().iterator().next();
        assertEquals(BookingStatus.CANCELLEDBYSTUDENT, booking.getStatus());
        verify(view).displaySuccess("Booking cancelled successfully.");
    }

    // CASE 3 booking doesnt belong to student
    // Must create another booking with another student
    @Test
    @DisplayName("Student fails to cancel booking, booking number doesn't exist")
    void testStudentCancelsBookingFailure_BookingNotStudents(){
        // EP registers and creates event
        TestHelper.loginAsEP(userController, view, verificationService);
        Event event = TestHelper.createTestEvent(eventPerformanceController, view);

        // student logins and books performance
        TestHelper.loginAsStudent(userController, view);
        TestHelper.bookTestPerformance(bookingController, view, paymentSystem);

        // login as second student
        TestHelper.loginAsStudent2(userController, view);
        TestHelper.bookTestPerformance(bookingController, view, paymentSystem);

        when(view.getInput("Enter Booking Number (or '-1' to return" +
                " back to dashboard: ")).thenReturn("1").thenReturn("2");
        when(view.getInput("Press ENTER to return to dashboard... \n")).thenReturn("");

        bookingController.cancelBooking();

        // create list to verify the first booking is still active and second is cancelled
        List<Booking> bookings = new ArrayList<>(bookingController.getBookings());
        Booking booking0 = bookings.get(0);
        Booking booking1 = bookings.get(1);

        // verify error is caught
        assertEquals(BookingStatus.ACTIVE, booking0.getStatus());
        verify(view).displayError("This booking number does not belong" +
                " to you. Please try again.");

        // verify successful cancellation after fix
        assertEquals(BookingStatus.CANCELLEDBYSTUDENT, booking1.getStatus());
        verify(view).displaySuccess("Booking cancelled successfully.");
    }
    // ERROR test failing because student is hardcoded
    // FIX add a student2 to preRegisteredUsers and loginAsStudent2 to TestHelper

    // CASE 3 booking is no longer active
    // must create a booking then cancel it, then try cancel it again
    @Test
    @DisplayName("Student fails to cancel booking, booking is not active")
    void testStudentCancelsBookingFailure_BookingNotActive(){
        // EP registers and creates event
        TestHelper.loginAsEP(userController, view, verificationService);
        Event event = TestHelper.createTestEvent(eventPerformanceController, view);

        // student logins and books performance
        TestHelper.loginAsStudent(userController, view);
        TestHelper.bookTestPerformance(bookingController, view, paymentSystem);

        // can now begin cancelling booking
        when(view.getInput("Enter Booking Number (or '-1' to return" +
                " back to dashboard: ")).thenReturn("1");
        when(view.getInput("Press ENTER to return to dashboard... \n")).thenReturn("");
        bookingController.cancelBooking();

        // can now begin cancelling booking
        when(view.getInput("Enter Booking Number (or '-1' to return" +
                " back to dashboard: ")).thenReturn("1").thenReturn("-1");
        bookingController.cancelBooking();

        Booking booking = bookingController.getBookings().iterator().next();
        assertEquals(BookingStatus.CANCELLEDBYSTUDENT, booking.getStatus());
        verify(view).displayError("This booking has been cancelled." +
                " Sorry for the inconvenience.");
    }

    // CASE 4 invalid booking number entered
    @Test
    @DisplayName("Student fails to cancel booking, invalid booking number")
    void testStudentCancelsBookingFailure_InvalidBookingNumber(){
        // EP registers and creates event
        TestHelper.loginAsEP(userController, view, verificationService);
        Event event = TestHelper.createTestEvent(eventPerformanceController, view);

        // student logins and books performance
        TestHelper.loginAsStudent(userController, view);
        TestHelper.bookTestPerformance(bookingController, view, paymentSystem);

        // can now begin cancelling booking
        when(view.getInput("Enter Booking Number (or '-1' to return" +
                " back to dashboard: ")).thenReturn("abc").thenReturn("1");
        when(view.getInput("Press ENTER to return to dashboard... \n")).thenReturn("");

        bookingController.cancelBooking();

        // verify error is caught
        verify(view).displayError("Invalid booking number." +
                " Please enter a valid number.");

        // verify successful cancellation after fix
        Booking booking = bookingController.getBookings().iterator().next();
        assertEquals(BookingStatus.CANCELLEDBYSTUDENT, booking.getStatus());
        verify(view).displaySuccess("Booking cancelled successfully.");
    }

    // CASE 5 performance is less than 24 hours away
    // must create a new performance that is less than 24 hours away
    // must hardcode it so the event is always less than 24 hours away
    @Test
    @DisplayName("Student fails to cancel booking, less than 24 hours away")
    void testStudentCancelsBookingFailure_BookingLessThan24(){
        String startDate = LocalDateTime.now().plusHours(12)
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        // EP registers and creates event
        TestHelper.loginAsEP(userController, view, verificationService);
        when(view.getInput("\nEnter Event Title: ")).thenReturn("Test Concert");
        when(view.getInput("Enter Choice: ")).thenReturn("1");
        when(view.getInput("\nIs this event ticketed? (Yes/No): ")).thenReturn("Yes");
        when(view.getInput("\nNumber of performances: ")).thenReturn("1");
        when(view.getInput("Enter Start Date (dd/MM/yyyy): ")).thenReturn(startDate);
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
        eventPerformanceController.createEvent();

        // student logins and books performance
        TestHelper.loginAsStudent(userController, view);
        TestHelper.bookTestPerformance(bookingController, view, paymentSystem);

        // can now begin cancelling booking
        when(view.getInput("Enter Booking Number (or '-1' to return" +
                " back to dashboard: ")).thenReturn("1");

        bookingController.cancelBooking();

        // verify error is caught and booking is still active
        Booking booking = bookingController.getBookings().iterator().next();
        assertEquals(BookingStatus.ACTIVE, booking.getStatus());
        verify(view).displayError("Cancellation is not allowed." +
                " The performance is less than 24 hours away.");
    }

    // CASE 6 refund fails
    @Test
    @DisplayName("Student fails to cancel booking, refund fails")
    void testStudentCancelsBookingFailure_RefundFails(){
        PaymentSystem paymentSystemFailure = mock(PaymentSystem.class);
        when(paymentSystemFailure.processPayment(anyInt(), anyString(), anyString()
                , anyInt(), anyString(), anyDouble())).thenReturn(false);

        bookingController = new BookingController(bookings, performances, view, paymentSystemFailure);

        // EP registers and creates event
        TestHelper.loginAsEP(userController, view, verificationService);
        Event event = TestHelper.createTestEvent(eventPerformanceController, view);

        // student logins and books performance
        TestHelper.loginAsStudent(userController, view);
        TestHelper.bookTestPerformance(bookingController, view, paymentSystem);

        // can now begin cancelling booking
        when(view.getInput("Enter Booking Number (or '-1' to return" +
                " back to dashboard: ")).thenReturn("1");

        bookingController.cancelBooking();

        // verify error in refund
        Booking booking = bookingController.getBookings().iterator().next();
        assertEquals(BookingStatus.ACTIVE, booking.getStatus());
        verify(view).displayError("Refund failed. Booking is not cancelled.");
    }

     // EARLY RETURN CASE EP enters -1 to return/exit
     @Test
     @DisplayName("Student enter -1 to return/exit")
     void testStudentCancelsBookingExit(){
         // EP registers and creates event
         TestHelper.loginAsEP(userController, view, verificationService);
         Event event = TestHelper.createTestEvent(eventPerformanceController, view);

         // student logins and books performance
         TestHelper.loginAsStudent(userController, view);
         TestHelper.bookTestPerformance(bookingController, view, paymentSystem);

         // can now begin cancelling booking
         when(view.getInput("Enter Booking Number (or '-1' to return" +
                 " back to dashboard: ")).thenReturn("-1");

         bookingController.cancelBooking();

         // verify no cancellation
         Booking booking = bookingController.getBookings().iterator().next();
         assertEquals(BookingStatus.ACTIVE, booking.getStatus());
         verify(view, never()).displaySuccess("Booking cancelled successfully.");
     }
}