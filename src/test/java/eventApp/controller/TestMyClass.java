package eventApp.controller;

import eventApp.model.Booking;
import eventApp.model.Student;
import eventApp.model.Performance;
import eventApp.model.Event;
import eventApp.enums.BookingStatus;
import eventApp.enums.EventType;
import eventApp.enums.PerformanceStatus;
import eventApp.external.MockPaymentSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.Arrays;

public class TestMyClass {

    // test fields
    private Student student1;
    private Student student2;
    private Event dummyEvent;
    private Performance performance;
    private Booking booking;

    // fields for performance testing
    private Booking activeBooking;
    private Booking cancelledBooking;

    // Payment System Test Fields
    private MockPaymentSystem paymentSystem;
    private final int validTickets = 2;
    private final String validEventTitle = "Comedy Night";
    private final String validStudentEmail = "s1234567@ed.ac.uk";
    private final int validStudentPhone = 1234567890;
    private final String validEpEmail = "provider@entertainment.com";
    private final double validAmount = 30.00;
    private final String validMessage = "Event cancelled due to weather.";

    @BeforeEach
    void setUp() {
        // test setup
        student1 = new Student("student@test.com", "password123", "student", 1234567890);
        student2 = new Student("abc@test.com", "abc123", "abc", 1243567898);
        dummyEvent = new Event(1, "Test Event Title", EventType.DANCE, true, "ep@test.com", "EP");

        // performance setup
        performance = new Performance(1,
                LocalDateTime.of(2026,4,21,10,0),
                LocalDateTime.of(2026,4,21,14,0),
                Arrays.asList("J"),
                "Hindeburgh",
                1234, false, false, 100, 10.00); // 100 tickets total

        performance.setEvent(dummyEvent);

        // booking setup
        booking = new Booking(1, 2, 20.00, LocalDateTime.of(2026,4,20,10,0));
        booking.setStudent(student1);
        booking.setPerformance(performance);

        activeBooking = new Booking(10, 2, 30.00, LocalDateTime.now());
        activeBooking.setStudent(student1);

        cancelledBooking = new Booking(11, 1, 15.00, LocalDateTime.now());
        cancelledBooking.setStudent(student1);
        cancelledBooking.cancelByStudent();

        // Mock Payement System
        paymentSystem = new MockPaymentSystem();
    }

    // Test Bookigs

    @Test
    @DisplayName("New booking starts with ACTIVE")
    void newBooking() {
        assertEquals(BookingStatus.ACTIVE, booking.getStatus(),
                "New booking starts with ACTIVE");
    }

    @Test
    @DisplayName("cancelByStudent sets the status to CANCELLEDBYSTUDENT")
    void cancelByStudent() {
        booking.cancelByStudent();
        assertEquals(BookingStatus.CANCELLEDBYSTUDENT, booking.getStatus(),
                "cancelByStudent sets the status to CANCELLEDBYSTUDENT");
    }

    @Test
    @DisplayName("cancelPaymentFailed sets the status to PAYMENTFAILED")
    void cancelPaymentFailed() {
        booking.cancelPaymentFailed();
        assertEquals(BookingStatus.PAYMENTFAILED, booking.getStatus(),
                "cancelPaymentFailed sets the status to PAYMENTFAILED");
    }

    @Test
    @DisplayName("cancelByProvider sets the status to CANCELLEDBYPROVIDER")
    void cancelByProvider() {
        booking.cancelByProvider();
        assertEquals(BookingStatus.CANCELLEDBYPROVIDER, booking.getStatus(),
                "cancelByProvider sets the status to CANCELLEDBYPROVIDER");
    }

    @Test
    @DisplayName("checkBookedByStudent() returns true for booking owner's email")
    void checkBookedByStudentCorrectEmail() {
        assertTrue(booking.checkBookedByStudent("student@test.com"),
                "Should return true when the email matches the booking's student email");
    }

    @Test
    @DisplayName("checkBookedByStudent returns false for different student email")
    void checkBookedByStudentDifferentEmail() {
        assertFalse(booking.checkBookedByStudent("abc@test.com"),
                "Should return false when the email does not match the booking's student email");
    }

    @Test
    @DisplayName("checkBookedByStudent returns false for an empty email")
    void checkBookedByStudentEmptyEmail() {
        assertFalse(booking.checkBookedByStudent(""),
                "Return false when there is an empty email");
    }

    @Test
    @DisplayName("checkBookedByStudent is case-sensitive")
    void checkBookedByStudentIsCaseSensitive() {
        assertFalse(booking.checkBookedByStudent("Student@test.com"),
                "Return false because of case-sensitive");
    }

    @Test
    @DisplayName("checkBookedByStudent return false when there is a null email")
    void checkBookedByStudentNullEmail() {
        assertFalse(booking.checkBookedByStudent(null),
                "Return false when there is a null email");
    }

    @Test
    @DisplayName("getStudentDetails return correct email at index 0")
    void getStudentDetailsCorrectEmail() {
        String[] details = booking.getStudentDetails();
        assertEquals("student@test.com", details[0],
                "Email has to be at index 0");
    }

    @Test
    @DisplayName("getStudentDetails return phone number at index 1")
    void getStudentDetailsCorrectPhoneNumber() {
        String[] details = booking.getStudentDetails();
        assertEquals("1234567890", details[1],
                "Phone number has to be at index 1");
    }

    @Test
    @DisplayName("getStudentDetails has to return array of length 2")
    void getStudentDetailsLength(){
        String[] details = booking.getStudentDetails();
        assertEquals(2, details.length,
                "Student details length has to be 2");
    }

    @Test
    @DisplayName("getStudentDetails returns empty string when no student is set")
    void getStudentDetailsEmptyString(){
        Booking noStudent = new Booking(2, 1243, 1.00, LocalDateTime.now());
        String[] details = noStudent.getStudentDetails();
        assertEquals("", details[0], "Student email has to be empty string");
        assertEquals("0", details[1], "Phone number has to be 0");
    }

    @Test
    @DisplayName("getStudentDetails return correct details for students")
    void getStudentDetailsCorrectDetails(){
        booking.setStudent(student2);
        String[] details = booking.getStudentDetails();
        assertEquals("abc@test.com", details[0], "Email should match student2");
        assertEquals("1243567898" , details[1], "Phone number should match student2");
    }

    @Test
    @DisplayName("generateBookingRecord contains booking number")
    void generateBookingRecordContainsBookingNumber() {
        assertTrue(booking.generateBookingRecord().contains("1"),
                "Booking record contains booking number");
    }

    @Test
    @DisplayName("generateBookingRecord contains event title")
    void generateBookingRecordContainsEventTitle() {
        assertTrue(booking.generateBookingRecord().contains("Test Event Title"),
                "Booking record contains event title");
    }

    @Test
    @DisplayName("generateBookingRecord reflects updated status after cancellation")
    void generateBookingRecordContainsTheUpdatedStatusAfterCancellation() {
        booking.cancelByStudent();
        assertTrue(booking.generateBookingRecord().contains("CANCELLEDBYSTUDENT"),
                "Booking record reflects the updated status after cancellation");
    }


    // Mock Payment System


    @Test
    @DisplayName("Payment: Valid inputs should return true")
    public void testProcessPayment_ValidInputs_ReturnsTrue() {
        assertTrue(paymentSystem.processPayment(
                validTickets, validEventTitle, validStudentEmail,
                validStudentPhone, validEpEmail, validAmount
        ));
    }

    @Test
    @DisplayName("Payment: 0 tickets should return false")
    public void testProcessPayment_ZeroTickets_ReturnsFalse() {
        assertFalse(paymentSystem.processPayment(
                0, validEventTitle, validStudentEmail,
                validStudentPhone, validEpEmail, validAmount
        ));
    }

    @Test
    @DisplayName("Payment: Negative amount should return false")
    public void testProcessPayment_NegativeAmount_ReturnsFalse() {
        assertFalse(paymentSystem.processPayment(
                validTickets, validEventTitle, validStudentEmail,
                validStudentPhone, validEpEmail, -15.50
        ));
    }

    @Test
    @DisplayName("Payment: Null email should return false")
    public void testProcessPayment_NullStudentEmail_ReturnsFalse() {
        assertFalse(paymentSystem.processPayment(
                validTickets, validEventTitle, null,
                validStudentPhone, validEpEmail, validAmount
        ));
    }

    @Test
    @DisplayName("Refund: Valid inputs with message should return true")
    public void testProcessRefund_ValidInputsWithMessage_ReturnsTrue() {
        assertTrue(paymentSystem.processRefund(
                validTickets, validEventTitle, validStudentEmail,
                validStudentPhone, validEpEmail, validAmount, validMessage
        ));
    }

    @Test
    @DisplayName("Refund: Null message should still succeed")
    public void testProcessRefund_NullMessage_ReturnsTrue() {
        assertTrue(paymentSystem.processRefund(
                validTickets, validEventTitle, validStudentEmail,
                validStudentPhone, validEpEmail, validAmount, null
        ));
    }


    // Performance Test


    @Test
    @DisplayName("Performance Constructor: Initializes with correct default values")
    public void testPerfConstructor_InitializesDefaultsCorrectly() {
        assertEquals(0, performance.getNumTicketsSold(), "Newly created performance should have 0 tickets sold.");
        assertFalse(performance.isSponsored(), "Newly created performance should not be sponsored.");
        assertEquals(PerformanceStatus.ACTIVE, performance.getStatus(), "Initial status must be ACTIVE.");
    }

    @Test
    @DisplayName("Performance: cancel() changes status to CANCELLED")
    public void testPerfCancel_UpdatesStatusToCancelled() {
        performance.cancel();
        assertEquals(PerformanceStatus.CANCELLED, performance.getStatus(),
                "cancel() must change the performance status to CANCELLED.");
    }

    @Test
    @DisplayName("Performance: checkIfEventIsTicketed returns true for ticketed event")
    public void testCheckIfEventIsTicketed_TicketedEvent_ReturnsTrue() {
        assertTrue(performance.checkIfEventIsTicketed(), "Should return true when associated event is ticketed.");
    }

    @Test
    @DisplayName("Performance: checkIfEventIsTicketed handles null event safely")
    public void testCheckIfEventIsTicketed_NullEvent_ReturnsFalse() {
        performance.setEvent(null);
        assertFalse(performance.checkIfEventIsTicketed(), "Should return false safely if the event is null.");
    }

    @Test
    @DisplayName("Performance: checkIfTicketsLeft returns true for exact remaining tickets")
    public void testCheckIfTicketsLeft_ExactAmount_ReturnsTrue() {
        performance.setNumTicketsSold(95); // 5 tickets left out of 100
        assertTrue(performance.checkIfTicketsLeft(5), "Should return true when requesting exact remaining tickets.");
    }

    @Test
    @DisplayName("Performance: checkIfTicketsLeft returns false when requesting too many")
    public void testCheckIfTicketsLeft_ExceedsCapacity_ReturnsFalse() {
        performance.setNumTicketsSold(95);
        assertFalse(performance.checkIfTicketsLeft(6), "Should return false when requesting more tickets than available.");
    }

    @Test
    @DisplayName("Performance: getFinalTicketPrice returns reduced price when sponsored")
    public void testGetFinalTicketPrice_Sponsored_ReturnsReducedPrice() {
        performance.sponsor(2.00);
        assertEquals(8.00, performance.getFinalTicketPrice(), 0.001, "Should deduct the sponsored amount from the base price.");
    }

    @Test
    @DisplayName("Performance: checkHasNotHappenedYet returns true for future performance")
    public void testCheckHasNotHappenedYet_FutureDate_ReturnsTrue() {
        assertTrue(performance.checkHasNotHappenedYet(), "Should return true since start time is in the future.");
    }

    @Test
    @DisplayName("Performance: checkCreatedByEP returns true for matching email")
    public void testCheckCreatedByEP_MatchingEmail_ReturnsTrue() {
        assertTrue(performance.checkCreatedByEP("ep@test.com"), "Should return true when EP email matches event organiser.");
    }

    @Test
    @DisplayName("Performance: hasActiveBookings returns true when active bookings exist")
    public void testHasActiveBookings_WithActive_ReturnsTrue() {
        performance.addBooking(cancelledBooking);
        performance.addBooking(activeBooking);
        assertTrue(performance.hasActiveBookings(), "Should return true if at least one booking is ACTIVE.");
    }

    @Test
    @DisplayName("Performance: hasActiveBookings returns false when only cancelled exist")
    public void testHasActiveBookings_OnlyCancelled_ReturnsFalse() {
        performance.addBooking(cancelledBooking);
        assertFalse(performance.hasActiveBookings(), "Should return false if all bookings are cancelled.");
    }

    @Test
    @DisplayName("Performance: getBookingDetailsForRefund only includes ACTIVE bookings")
    public void testGetBookingDetailsForRefund_FormatsCorrectly() {
        performance.addBooking(cancelledBooking); // Ignored
        performance.addBooking(activeBooking);    // Included

        String details = performance.getBookingDetailsForRefund();
        assertTrue(details.contains("student@test.com"), "Must contain the active student's email.");
        assertTrue(details.endsWith(";"), "Each record must end with a semicolon as defined by the format string.");
    }

    @Test
    @DisplayName("Performance: review successfully adds ratings and comments")
    public void testReview_AddsDataToLists() {
        performance.review(5, "Great show!");
        assertEquals(1, performance.getReviewRatings().size(), "Review ratings list should contain 1 item.");
        assertTrue(performance.getReviewComments().contains("Great show!"), "Comments list should contain provided text.");
    }
}