package eventApp.controller;

import eventApp.enums.BookingStatus;
import eventApp.enums.EventType;
import eventApp.model.Booking;
import eventApp.model.Event;
import eventApp.model.Performance;
import eventApp.model.Student;
import eventApp.external.MockPaymentSystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class TestMyClass {

    //preregistered student accounts
    private Student s1;
    private Student s2;

    private Performance perf;
    private Booking myBooking;

    private MockPaymentSystem mockPay;
    private int tix = 2;
    private double cost = 20.00;
    private String evName = "Test Event Title";
    private String providerMail = "ep@test.com";

    @BeforeEach
    void setUp() {
        //student details
        s1 = new Student("student@test.com", "password123", "student", 1234567890);
        s2 = new Student("abc@test.com", "abc123", "abc", 1243567898);

        //create event
        Event ev =  new Event(1, "Test Event Title", EventType.DANCE, true, "ep@test.com", "EP");

        //performance
        perf = new Performance(1,
                LocalDateTime.of(2026,4,21,10,0),
                LocalDateTime.of(2026,4,21,14,0),
                Arrays.asList("J"),
                "Hindeburgh",
                1234, false, false, 1234, 10.00);

        perf.setEvent(ev);

        //booking
        myBooking = new Booking(1, 2, 20.00, LocalDateTime.of(2026,4,20,10,0));
        myBooking.setStudent(s1);
        myBooking.setPerformance(perf);

        // setup mock payment for later
        mockPay = new MockPaymentSystem();
    }

    //Initial state
    @Test
    @DisplayName("make sure a new booking is active")
    void newBooking() {
        assertEquals(BookingStatus.ACTIVE, myBooking.getStatus(),
                "status should just be ACTIVE at the start");
    }

    //checkBookedByStudent
    @Test
    @DisplayName("should fail if we pass a null email")
    void checkBookedByStudentNullEmail() {
        assertFalse(myBooking.checkBookedByStudent(null),
                "cant check a null email, should be false");
    }

    @Test
    @DisplayName("returns true for the actual owner's email")
    void checkBookedByStudentCorrectEmail() {
        assertTrue(myBooking.checkBookedByStudent("student@test.com"),
                "this is the right email so it needs to return true");
    }

    @Test
    @DisplayName("fails if the email is empty")
    void checkBookedByStudentEmptyEmail() {
        assertFalse(myBooking.checkBookedByStudent(""),
                "empty string shouldn't match the student email");
    }

    @Test
    @DisplayName("fails for a different random email")
    void checkBookedByStudentDifferentEmail() {
        assertFalse(myBooking.checkBookedByStudent("abc@test.com"),
                "wrong email should definitely return false");
    }

    @Test
    @DisplayName("email check should be case sensitive")
    void checkBookedByStudentIsCaseSensitive() {
        assertFalse(myBooking.checkBookedByStudent("Student@test.com"),
                "capital S should make it return false");
    }

    //getStudentDetails
    @Test
    @DisplayName("checks if the array size is exactly 2")
    void getStudentDetailsLength(){
        String[] info = myBooking.getStudentDetails();
        assertEquals(2, info.length,
                "the array needs to only have 2 spots");
    }

    @Test
    @DisplayName("makes sure phone number is in index 1")
    void getStudentDetailsCorrectPhoneNumber() {
        String[] info = myBooking.getStudentDetails();
        assertEquals("1234567890", info[1],
                "second item should be the phone string");
    }

    @Test
    @DisplayName("handles when a student isnt assigned yet")
    void getStudentDetailsEmptyString(){
        Booking emptyBook = new Booking(2, 1243, 1.00, LocalDateTime.now());
        String[] info = emptyBook.getStudentDetails();
        assertEquals("", info[0], "should be blank if no student");
        assertEquals("0", info[1], "phone should default to 0");
    }

    @Test
    @DisplayName("makes sure email is in index 0")
    void getStudentDetailsCorrectEmail() {
        String[] info = myBooking.getStudentDetails();
        assertEquals("student@test.com", info[0],
                "first item should be the email string");
    }

    @Test
    @DisplayName("works correctly when swapping to student 2")
    void getStudentDetailsCorrectDetails(){
        myBooking.setStudent(s2);
        String[] info = myBooking.getStudentDetails();
        assertEquals("abc@test.com", info[0], "email should change to s2");
        assertEquals("1243567898" , info[1], "phone should change to s2");
    }

    //cancel payment failed
    @Test
    @DisplayName("cancel because payment failed updates status")
    void cancelPaymentFailed() {
        myBooking.cancelPaymentFailed();
        assertEquals(BookingStatus.PAYMENTFAILED, myBooking.getStatus(),
                "status should change to payment failed");
    }

    //cancel by provider
    @Test
    @DisplayName("provider cancelling changes status properly")
    void cancelByProvider() {
        myBooking.cancelByProvider();
        assertEquals(BookingStatus.CANCELLEDBYPROVIDER, myBooking.getStatus(),
                "should show the provider cancelled it");
    }

    //cancel by student
    @Test
    @DisplayName("student cancelling changes status properly")
    void cancelByStudent() {
        myBooking.cancelByStudent();
        assertEquals(BookingStatus.CANCELLEDBYSTUDENT, myBooking.getStatus(),
                "should show the student cancelled it");
    }

    //generateBookingNumber
    @Test
    @DisplayName("record string has the header")
    void generateBookingRecordContainsTheBookingHeader() {
        assertTrue(myBooking.generateBookingRecord().contains("BOOKING RECORD"),
                "needs to have the title header at the top");
    }

    @Test
    @DisplayName("record string has the price paid")
    void generateBookingRecordContainsAmountPaid() {
        assertTrue(myBooking.generateBookingRecord().contains("20.00"),
                "should print out the 20 quid paid");
    }

    @Test
    @DisplayName("record string updates if provider cancels")
    void generateBookingRecordContainsTheUpdatedCancelledProvider() {
        myBooking.cancelByProvider();
        assertTrue(myBooking.generateBookingRecord().contains("CANCELLEDBYPROVIDER"),
                "record should update if EP cancels it");
    }

    @Test
    @DisplayName("record string has the venue")
    void generateBookingRecordContainsTheVenueAddress() {
        assertTrue(myBooking.generateBookingRecord().contains("Hindeburgh"),
                "venue location should be printed");
    }

    @Test
    @DisplayName("record string updates if student cancels")
    void generateBookingRecordContainsTheUpdatedStatusAfterCancellation() {
        myBooking.cancelByStudent();
        assertTrue(myBooking.generateBookingRecord().contains("CANCELLEDBYSTUDENT"),
                "record should update if student cancels");
    }

    @Test
    @DisplayName("record string has ticket amount")
    void generateBookingRecordContainsNumberOfTickets() {
        assertTrue(myBooking.generateBookingRecord().contains("2"),
                "needs to show they bought 2 tickets");
    }

    @Test
    @DisplayName("record string has the event title")
    void generateBookingRecordContainsEventTitle() {
        assertTrue(myBooking.generateBookingRecord().contains("Test Event Title"),
                "the name of the event should be in there");
    }

    @Test
    @DisplayName("record string updates if payment fails")
    void generateBookingRecordContainsTheUpdatedPaymentFailed() {
        myBooking.cancelPaymentFailed();
        assertTrue(myBooking.generateBookingRecord().contains("PAYMENTFAILED"),
                "should say payment failed in the string");
    }

    @Test
    @DisplayName("record string has the current status")
    void generateBookingRecordContainsTheBookingStatus() {
        assertTrue(myBooking.generateBookingRecord().contains("ACTIVE"),
                "status line should say ACTIVE");
    }

    @Test
    @DisplayName("record string has the start date")
    void generateBookingRecordContainsStartingDateAndTime() {
        assertTrue(myBooking.generateBookingRecord().contains("21/04/2026 10:00"),
                "performance start time should be shown");
    }

    @Test
    @DisplayName("record string has the booking ID")
    void generateBookingRecordContainsBookingNumber() {
        assertTrue(myBooking.generateBookingRecord().contains("1"),
                "ID number 1 should be visible");
    }

    @Test
    @DisplayName("record string has the date booked")
    void generateBookingRecordContainsTheDateBooked() {
        assertTrue(myBooking.generateBookingRecord().contains("20/04/2026 10:00"),
                "booking timestamp needs to be there");
    }




    // MockPaymentSystem


    @Test
    @DisplayName("refund works with normal data")
    public void testRefundWorksFine() {
        assertTrue(mockPay.processRefund(tix, evName, "student@test.com", 1234567890, providerMail, cost, "sorry!"),
                "a normal refund should just return true");
    }

    @Test
    @DisplayName("payment fails if we pass a negative cost")
    public void testPaymentFailsNegativeMoney() {
        assertFalse(mockPay.processPayment(tix, evName, "student@test.com", 1234567890, providerMail, -50.00),
                "cant pay negative money, should block it");
    }

    @Test
    @DisplayName("payment works properly with valid data")
    public void testPaymentWorksFine() {
        assertTrue(mockPay.processPayment(tix, evName, "student@test.com", 1234567890, providerMail, cost),
                "a normal payment should go through and return true");
    }

    @Test
    @DisplayName("payment fails if tickets are 0")
    public void testPaymentFailsZeroTix() {
        assertFalse(mockPay.processPayment(0, evName, "student@test.com", 1234567890, providerMail, cost),
                "shouldnt be able to process a payment for 0 tickets");
    }

    @Test
    @DisplayName("refund works even if there is no message")
    public void testRefundNullMessage() {
        assertTrue(mockPay.processRefund(tix, evName, "student@test.com", 1234567890, providerMail, cost, null),
                "null message is allowed so refund should still work");
    }

    @Test
    @DisplayName("payment fails if the student email is missing")
    public void testPaymentFailsNullEmail() {
        assertFalse(mockPay.processPayment(tix, evName, null, 1234567890, providerMail, cost),
                "null email should break it and return false");
    }
}