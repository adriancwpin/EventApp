package eventApp.controller;

import eventApp.enums.BookingStatus;
import eventApp.enums.EventType;
import eventApp.model.Booking;
import eventApp.model.Event;
import eventApp.model.Performance;
import eventApp.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class TestBooking {

    //hardcode the preregistered student accounts
    private Student student1;
    private Student student2;

    private Performance performance;
    private Booking booking;

    @BeforeEach
    void setUp() {
        //student details
        student1 = new Student("student@test.com", "password123", "student", 1234567890);
        student2 = new Student("abc@test.com", "abc123", "abc", 1243567898);

        //create event
        Event event =  new Event(1, "Test Event Title", EventType.DANCE, true, "ep@test.com", "EP");

        //performance
        performance = new Performance(1,
                LocalDateTime.of(2026,4,21,10,0),
                LocalDateTime.of(2026,4,21,14,0),
                Arrays.asList("J"),
                "Hindeburgh",
                1234, false, false, 1234, 10.00);

        performance.setEvent(event); //this performance belongs to this specific event

        //booking
        booking = new Booking(1, 2, 20.00, LocalDateTime.of(2026,4,20,10,0));
        booking.setStudent(student1);
        booking.setPerformance(performance); //this booking belongs to this performance
    }

    //Initial state

    @Test
    @DisplayName("New booking starts with ACTIVE")
    void newBooking() {
        assertEquals(BookingStatus.ACTIVE, booking.getStatus(),
                "New booking starts with ACTIVE");
    }

    //cancel by student
    @Test
    @DisplayName("cancelByStudent sets the status to CANCELLEDBYSTUDENT")
    void cancelByStudent() {
        booking.cancelByStudent();
        assertEquals(BookingStatus.CANCELLEDBYSTUDENT, booking.getStatus(),
                "cancelByStudent sets the status to CANCELLEDBYSTUDENT");
    }

    //cancel payment failed
    @Test
    @DisplayName("cancelPaymentFailed sets the status to PAYMENTFAILED")
    void cancelPaymentFailed() {
        booking.cancelPaymentFailed();
        assertEquals(BookingStatus.PAYMENTFAILED, booking.getStatus(),
                "cancelPaymentFailed sets the status to PAYMENTFAILED");
    }

    //cancel by provider
    @Test
    @DisplayName("cancelByProvider sets the status to CANCELLEDBYPROVIDER")
    void cancelByProvider() {
        booking.cancelByProvider();
        assertEquals(BookingStatus.CANCELLEDBYPROVIDER, booking.getStatus(),
                "cancelByProvider sets the status to CANCELLEDBYPROVIDER");
    }

    //checkBookedByStudent

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

    //cancelByStudent get overrides by cancelByProvider
    @Test
    @DisplayName("CancelledByProvider overides CancelledByStudent")
    void cancelByProviderOverides() {
        booking.cancelByStudent();
        booking.cancelByProvider();

        assertEquals(BookingStatus.CANCELLEDBYPROVIDER , booking.getStatus(),
                "cancelByProvider overides CancelledByStudent");
    }

    //getStudentDetails
    @Test
    @DisplayName("getStudentDetails return correct email at index 0")
    void getStudentDetailsCorrectEmailForStudent1() {
        String[] details = booking.getStudentDetails();
        assertEquals("student@test.com", details[0],
                "Email has to be at index 0");
    }

    @Test
    @DisplayName("getStudentDetails return phone number at index 1")
    void getStudentDetailsCorrectPhoneNumberForStudent1() {
        String[] details = booking.getStudentDetails();
        assertEquals("1234567890", details[1],
                "Phone number has to be at index 1");
    }

    @Test
    @DisplayName("getStudentDetails returns correct email for student2")
    void getStudentDetailsCorrectEmailForStudent2(){
        booking.setStudent(student2);
        String[] details = booking.getStudentDetails();
        assertEquals("abc@test.com", details[0], "Email should match student2");
    }

    @Test
    @DisplayName("getStudentDetails returns correct phone number for student2")
    void getStudentDetailsCorrectPhoneNumberForStudent2(){
        booking.setStudent(student2);
        String[] details = booking.getStudentDetails();
        assertEquals("1243567898", details[1], "Phone number should match student2");
    }

    @Test
    @DisplayName("getStudentDetails has to return array of length 2")
    void getStudentDetailsLength(){
        String[] details = booking.getStudentDetails();
        assertEquals(2, details.length,
                "Student details length has to be 2");
    }

    @Test
    @DisplayName("getStudentDetails returns empty email when no student is set")
    void getStudentDetailsEmptyEmail(){
        //create a new booking object where no student book
        Booking noStudent = new Booking(2, 1243, 1.00, LocalDateTime.now());
        String[] details = noStudent.getStudentDetails();
        assertEquals("", details[0],
                "Student email has to be empty string");
    }

    @Test
    @DisplayName("getStudentDetails return 0 for phone number when no student is set")
    void getStudentDetailsEmptyPhoneNumber(){
        //create a new booking object where no student book
        Booking noStudent = new Booking(2, 1243, 1.00, LocalDateTime.now());
        String[] details = noStudent.getStudentDetails();
        assertEquals("0", details[1],
                "Student phone number has to be 0");
    }

    //generateBookingNumber
    @Test
    @DisplayName("generateBookingRecord contains booking number")
    void generateBookingRecordContainsBookingNumber() {
        String bookingRecord = booking.generateBookingRecord();
        assertTrue(bookingRecord.contains("1"),
                "Booking record contains booking number");
    }

    @Test
    @DisplayName("generateBookingRecord contains event title")
    void generateBookingRecordContainsEventTitle() {
        String bookingRecord = booking.generateBookingRecord();
        assertTrue(bookingRecord.contains("Test Event Title"),
                "Booking record contains event title");
    }

    @Test
    @DisplayName("generateBookingRecord contains starting date and time")
    void generateBookingRecordContainsStartingDateAndTime() {
        String bookingRecord = booking.generateBookingRecord();
        assertTrue(bookingRecord.contains("21/04/2026 10:00"),
                "Booking record contains start date and time");
    }

    @Test
    @DisplayName("generateBookingRecord contains the venue address")
    void generateBookingRecordContainsTheVenueAddress() {
        String bookingRecord = booking.generateBookingRecord();
        assertTrue(bookingRecord.contains("Hindeburgh"),
                "Booking record contains the venue");
    }

    @Test
    @DisplayName("generateBookingRecord contains number of tickets")
    void generateBookingRecordContainsNumberOfTickets() {
        String bookingRecord = booking.generateBookingRecord();
        assertTrue(bookingRecord.contains("2"),
                "Booking record contains number of tickets");
    }

    @Test
    @DisplayName("generateBookingRecord contains amount paid")
    void generateBookingRecordContainsAmountPaid() {
        String bookingRecord = booking.generateBookingRecord();
        assertTrue(bookingRecord.contains("20.00"),
                "Booking record contains amount paid");
    }

    @Test
    @DisplayName("generateBookingRecord contains the date booked")
    void generateBookingRecordContainsTheDateBooked() {
        String bookingRecord = booking.generateBookingRecord();
        assertTrue(bookingRecord.contains("20/04/2026 10:00"),
                "Booking record contains the date booked");
    }

    @Test
    @DisplayName("generateBookingRecord contains the booking status")
    void generateBookingRecordContainsTheBookingStatus() {
        String bookingRecord = booking.generateBookingRecord();
        assertTrue(bookingRecord.contains("ACTIVE"),
                "Booking record contains the current booking status");
    }

    @Test
    @DisplayName("generateBookingRecord reflects the updated status after cancellation")
    void generateBookingRecordContainsTheUpdatedStatusAfterCancellation() {
        booking.cancelByStudent();
        String bookingRecord = booking.generateBookingRecord();
        assertTrue(bookingRecord.contains("CANCELLEDBYSTUDENT"),
                "Booking record reflects the updated status after cancellation");
    }

    @Test
    @DisplayName("generateBookingRecord reflects the updated PAYMENTFAILED")
    void generateBookingRecordContainsTheUpdatedPaymentFailed() {
        booking.cancelPaymentFailed();
        String bookingRecord = booking.generateBookingRecord();
        assertTrue(bookingRecord.contains("PAYMENTFAILED"), "Booking record reflects the updated PAYMENTFAILED");
    }

    @Test
    @DisplayName("generateBookingRecord reflects the updated CANCELLEDBYPROVIDER")
    void generateBookingRecordContainsTheUpdatedCancelledProvider() {
        booking.cancelByProvider();
        String bookingRecord = booking.generateBookingRecord();
        assertTrue(bookingRecord.contains("CANCELLEDBYPROVIDER"),
                "Booking record reflects the updated CANCELLEDBYPROVIDER");
    }

    @Test
    @DisplayName("generateBookingRecord contains the booking header")
    void generateBookingRecordContainsTheBookingHeader() {
        String bookingRecord = booking.generateBookingRecord();
        assertTrue(bookingRecord.contains("BOOKING RECORD"),
                "Booking record contains the booking header");
    }
}
