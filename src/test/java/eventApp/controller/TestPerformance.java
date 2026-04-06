package eventApp.controller;

import eventApp.enums.EventType;
import eventApp.enums.PerformanceStatus;
import eventApp.model.Booking;
import eventApp.model.Event;
import eventApp.model.Performance;
import eventApp.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;


import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

class TestPerformance {

    // for future performance
    private Performance futurePerf;
    //past performance
    private Performance pastPerf;

    //to check ticketed event
    private Event ticketed;
    private Event nonticketed;

    @BeforeEach
    void setUp() {
        List<String> performers = Arrays.asList("A");

        futurePerf = new Performance(1,
                LocalDateTime.of(2026, 04, 20, 10, 0),
                LocalDateTime.of(2026, 04, 30, 12,0),
                performers, "Test", 100, false, false, 80,
                12);

        pastPerf = new Performance(2,
                LocalDateTime.of(2026, 03, 21, 10, 0),
                LocalDateTime.of(2026, 03, 21, 11, 0),
                performers, "Test", 120, false, false, 100,
                10);

        ticketed = new Event(1, "Concert", eventApp.enums.EventType.MUSIC, true,
                "ep@test.com", "EP");

        nonticketed = new Event(2, "free", EventType.THEATRE, false, "ep@test.com", "EP");

        futurePerf.setEvent(ticketed);
    }

    //cancel
    @Test
    @DisplayName("cancel set the performance to CANCELLED")
    void cancelSetPerformanceToCANCELLED() {
        futurePerf.cancel();
        assertEquals(PerformanceStatus.CANCELLED, futurePerf.getStatus(),
                "Performance status should be CANCELLED");
    }

    @Test
    @DisplayName("cancel() on a cancelled performance should still stay on CANCELLED")
    void continueSetPerformanceToCANCELLED() {
        futurePerf.cancel();
        futurePerf.cancel();
        assertEquals(PerformanceStatus.CANCELLED, futurePerf.getStatus(),
                "Performance status should still be CANCELLED");
    }

    //checkIfEventIsTicketed()
    @Test
    @DisplayName("return true when event is ticketed")
    void returnTrueWhenEventIsTicketed() {
        futurePerf.setEvent(ticketed);
        assertTrue(futurePerf.checkIfEventIsTicketed(),
                "Return true when event is ticketed");
    }

    @Test
    @DisplayName("return false when event is not ticketed")
    void returnFalseWhenEventIsNotTicketed() {
        futurePerf.setEvent(nonticketed);
        assertFalse(futurePerf.checkIfEventIsTicketed(),
                "Return false when event is not ticketed");
    }

    @Test
    @DisplayName("return false when no event is set")
    void returnFalseWhenNoEventIsSet() {
        //create performance but no event associated
        Performance noEvent = new Performance(3,
                LocalDateTime.of(2026,05,01,12,0),
                LocalDateTime.of(2026, 05, 01, 15,0),
                Arrays.asList("B"), "edin", 123, false, false, 122,
                12);

        assertFalse(noEvent.checkIfEventIsTicketed(),
                "Return false when no event is set");
    }

    //checkIfTicketLeft()
    @Test
    @DisplayName("return true when there are enough ticket left")
    void returnTrueWhenThereAreEnoughTicketLeft() {
        //0 sold, request 5 tickets
        assertTrue(futurePerf.checkIfTicketsLeft(5),
                "Return true when there are enough ticket left");
    }

    @Test
    @DisplayName("return true when request all of the ticket with zero sold")
    void returnTrueWhenRequestAllOfTheTicketWithZeroSold() {
        //request all of the tickets with 0 sold
        assertTrue(futurePerf.checkIfTicketsLeft(80),
                "Return true when there are enough ticket left");
    }

    @Test
    @DisplayName("return false when request more than available")
    void returnFalseWhenRequestMoreThanAvailable() {
        //80tickets , request 81
        assertFalse(futurePerf.checkIfTicketsLeft(81),
                "Return false when there are not enough ticket left");
    }

    @Test
    @DisplayName("return false when all tickets sold out")
    void returnFalseWhenAllTicketsSoldOut() {
        futurePerf.setNumTicketsSold(80);
        assertFalse(futurePerf.checkIfTicketsLeft(1),
                "Return false when all tickets sold out");
    }

    //getFinalTicketPrice()
    @Test
    @DisplayName("return original price when not sponsored")
    void returnOriginalPriceWhenNotSponsored() {
        assertEquals(12.00, futurePerf.getFinalTicketPrice(),
                "Return original price when not sponsored");
    }

    @Test
    @DisplayName("return reduced price when sponsored")
    void returnReducedPriceWhenSponsored() {
        futurePerf.sponsor(2.00);
        assertEquals(10.00, futurePerf.getFinalTicketPrice(),
                "Return reduced price when sponsored");
    }

    @Test
    @DisplayName("return zero when sponsorship is full amount")
    void returnZeroWhenSponsorshipIsFullAmount() {
        futurePerf.sponsor(12.00);
        assertEquals(0.00, futurePerf.getFinalTicketPrice(),
                "Return zero when sponsorship is full amount");
    }

    @Test
    @DisplayName("return original price when sponsor is zero")
    void returnOriginalPriceWhenSponsorIsZero() {
        futurePerf.sponsor(0.00);
        assertEquals(12.00, futurePerf.getFinalTicketPrice(),
                "Return original price when sponsor is zero");
    }

    //getOrganiserEmail()
    @Test
    @DisplayName("getOrganiserEmail() returns the correct email when event is set")
    void returnTrueWhenTheOrganiserEmailIsTheSameAsEventCreated() {
        assertEquals("ep@test.com", futurePerf.getOrganiserEmail(),
                "getOrganiserEmail() returns the correct email when event is set");
    }

    @Test
    @DisplayName("return null when no event is created")
    void returnFalseWhenTheEventIsNull() {
        Performance noEvent = new Performance(3,
                LocalDateTime.of(2026, 05,03,10,0),
                LocalDateTime.of(2026,05,03,12,0),
                Arrays.asList("C"), "london", 1234, false, false, 1000,
                12.00);
        assertNull(noEvent.getOrganiserEmail(),
                "Return null when no event is created");
    }

    //getEventTitle()
    @Test
    @DisplayName("return the correct event title the performance belongs to")
    void returnTrueWhenTheEventTitleIsTheSameAsEventCreated() {
        assertEquals("Concert", futurePerf.getEventTitle(),
                "Return the correct event title the performance belongs to");
    }

    @Test
    @DisplayName("return Unknown Event when the event is not set")
    void returnTrueWhenTheEventIsNotSet() {
        Performance noEvent = new Performance(3,
                LocalDateTime.of(2026, 05,03,10,0),
                LocalDateTime.of(2026,05,03,12,0),
                Arrays.asList("C"), "london", 1234, false, false, 1000,
                12.00);
        assertEquals("Unknown Event", noEvent.getEventTitle(),
                "Return Unknown Event when the event is not set");
    }

    //checkHasNotHappenedYet
    @Test
    @DisplayName("return true for future performance")
    void returnTrueForFuturePerformance() {
        assertTrue(futurePerf.checkHasNotHappenedYet(),
                "Return true for future performance");
    }

    @Test
    @DisplayName("return false for past performances")
    void returnFalseForPastPerformances() {
        assertFalse(pastPerf.checkHasNotHappenedYet(),
                "Return false for past performances that already happened");
    }

    //checkCreatedByEP(String email)
    @Test
    @DisplayName("return true when email matches organiser email")
    void returnTrueWhenEmailMatchesOrganiserEmail() {
        assertTrue(futurePerf.checkCreatedByEP("ep@test.com"),
                "Return true when email matches organiser email");
    }

    @Test
    @DisplayName("return false when the email does not match")
    void returnFalseWhenEmailDoesNotMatch() {
        assertFalse(futurePerf.checkCreatedByEP("123@test.com"),
                "Return false when the email does not match");
    }

    @Test
    @DisplayName("return false when the email is empty")
    void returnFalseWhenEmailIsEmpty() {
        assertFalse(futurePerf.checkCreatedByEP(""),
                "Return false when the email is empty");
    }

    //hasActiveBooking()
    @Test
    @DisplayName("return false when there is no active booking")
    void returnFalseWhenThereIsNoActiveBooking() {
        assertFalse(futurePerf.hasActiveBookings(), "Return false when there is no active booking");
    }

    @Test
    @DisplayName("return true when there is at least one active booking")
    void returnTrueWhenThereIsAtLeastOneActiveBooking() {
        Booking booking = new Booking(1, 5, 60.00, LocalDateTime.now());
        futurePerf.addBooking(booking);
        assertTrue(futurePerf.hasActiveBookings(),
                "Return true when there is at least one active booking");
    }

    @Test
    @DisplayName("return false when the active bookings are full of cancelled bookings")
    void returnFalseWhenTheActiveBookingsAreFullOfCancelledBookings() {
        Booking cancelled = new Booking(1, 1, 12.00, LocalDateTime.now());
        cancelled.cancelByStudent();
        futurePerf.addBooking(cancelled);
        assertFalse(futurePerf.hasActiveBookings(),
                "return false when the active bookings are full of cancelled bookings");
    }

    @Test
    @DisplayName("return true when the bookings are mix of active and cancelled")
    void returnTrueWhenTheBookingsAreMixOfActiveAndCancelled() {
        Booking cancelled = new Booking(1, 1, 12.00, LocalDateTime.now());
        Booking booking = new Booking(2, 5, 60.00, LocalDateTime.now());
        cancelled.cancelByStudent();
        futurePerf.addBooking(cancelled);
        futurePerf.addBooking(booking);
        assertTrue(futurePerf.hasActiveBookings(),
                "return true when the bookings are mix of active and cancelled");
    }

    //getBookingDetailsForRefund()
    @Test
    @DisplayName("return empty string when there is no booking")
    void returnEmptyStringWhenThereIsNoBooking() {
        assertEquals("", futurePerf.getBookingDetailsForRefund(),
                "Return empty string when there is no booking");
    }

    @Test
    @DisplayName("return student details when there is booking")
    void returnStudentDetailsWhenThereIsBooking() {
        Student student = new Student("student@test.com", "pass", "student", 1234567890);
        Booking booking = new Booking(1, 5, 60.00, LocalDateTime.now());
        booking.setStudent(student);
        booking.setPerformance(futurePerf);
        futurePerf.addBooking(booking);

        String details = futurePerf.getBookingDetailsForRefund();
        assertTrue(details.contains("student@test.com"),
                "details contains student email");
    }

    @Test
    @DisplayName("return student phone number when there is booking")
    void returnStudentPhoneNumberWhenThereIsBooking() {
        Student student = new Student("student@test.com", "pass", "student", 1234567890);
        Booking booking = new Booking(1, 5, 60.00, LocalDateTime.now());
        booking.setStudent(student);
        booking.setPerformance(futurePerf);
        futurePerf.addBooking(booking);

        String details = futurePerf.getBookingDetailsForRefund();
        assertTrue(details.contains("1234567890"),
                "details contains student phone number");
    }

    @Test
    @DisplayName("return amount paid when there is booking")
    void returnAmountPaidWhenThereIsBooking() {
        Student student = new Student("student@test.com", "pass", "student", 1234567890);
        Booking booking = new Booking(1, 5, 60.00, LocalDateTime.now());
        booking.setStudent(student);
        booking.setPerformance(futurePerf);
        futurePerf.addBooking(booking);

        String details = futurePerf.getBookingDetailsForRefund();
        assertTrue(details.contains("60.00"),
                "details contains amount paid for the ticker");
    }

    @Test
    @DisplayName("return ticket bought when there is booking")
    void returnTicketBoughtWhenThereIsBooking() {
        Student student = new Student("student@test.com", "pass", "student", 1234567890);
        Booking booking = new Booking(1, 5, 60.00, LocalDateTime.now());
        booking.setStudent(student);
        booking.setPerformance(futurePerf);
        futurePerf.addBooking(booking);

        String details = futurePerf.getBookingDetailsForRefund();
        assertTrue(details.contains("5"),
                "details contains total number of ticket bought");
    }

    @Test
    @DisplayName("details excludes cancelled booking")
    void returnBookingDetailsExcludesCancelledBooking() {
        Student student = new Student("student@test.com", "pass", "student", 1234567890);
        Booking booking = new Booking(1, 5, 60.00, LocalDateTime.now());
        booking.setStudent(student);
        booking.setPerformance(futurePerf);
        booking.cancelByStudent();
        futurePerf.addBooking(booking);

        String details = futurePerf.getBookingDetailsForRefund();
        assertEquals("", details,
                "Cancelled bookings will not appear in booking refund details");
    }

    @Test
    @DisplayName("Only includes active bookings when there are mix of active and cancelled bookings")
    void returnBookingDetailsOnlyIncludesActiveBookings() {
        Student student = new Student("student@test.com", "pass", "student", 1234567890);
        Student student2 = new Student("student2@test.com", "pass2", "student2", 1234567809);

        Booking active = new Booking(1, 5, 60.00, LocalDateTime.now());
        active.setStudent(student);
        active.setPerformance(futurePerf);
        futurePerf.addBooking(active);

        Booking cancelled = new Booking(2, 5, 60.00, LocalDateTime.now());
        cancelled.setStudent(student2);
        cancelled.setPerformance(futurePerf);
        cancelled.cancelByStudent();
        futurePerf.addBooking(cancelled);

        String details = futurePerf.getBookingDetailsForRefund();
        assertTrue(details.contains("student@test.com"),
                "details contains active booking's student email");
        assertFalse(details.contains("student2@test.com"),
                "details does not contain cancelled booking's student email");
    }

    //sponsor()
    @Test
    @DisplayName("sponsor() set isSponsored to true")
    void sponsorSetIsSponsored() {
        futurePerf.sponsor(2.00);
        assertTrue(futurePerf.isSponsored(),
                "sponsor() set isSponsored to true");
    }

    @Test
    @DisplayName("sponsor() set the sponsorship amount correctly")
    void sponsorSetTheSponsorshipAmountCorrectly() {
        futurePerf.sponsor(2.00);
        assertEquals(2.00, futurePerf.getSponsoredAmount(),
                "sponsor() set the sponsorship amount correctly");
    }

    @Test
    @DisplayName("zero sponsor amount is also true")
    void zeroSponsorAmountIsAlsoTrue() {
        futurePerf.sponsor(0.00);
        assertTrue(futurePerf.isSponsored(),
                "zero sponsor amount is also true");
    }

    //checkBookPerfByStudent
    //empty string is already tested in TestBooking so prevent test duplication

    @Test
    @DisplayName("should return true when there is active booking by student")
    void shouldReturnTrueWhenActiveBookingByStudent() {
        Student student = new Student("student@test.com", "pass", "student", 1234567890);
        Booking booking = new Booking(1, 5, 60.00, LocalDateTime.now());
        booking.setStudent(student);
        booking.setPerformance(futurePerf);
        futurePerf.addBooking(booking);

        assertTrue(futurePerf.checkBookedPerfByStudent("student@test.com"),
                "should return true when there is active booking by student");
    }

    @Test
    @DisplayName("should return false when the booking is cancelled")
    void shouldReturnFalseWhenBookingIsCancelled() {
        Student student = new Student("student@test.com", "pass", "student", 1234567890);
        Booking booking = new Booking(1, 5, 60.00, LocalDateTime.now());
        booking.setStudent(student);
        booking.setPerformance(futurePerf);
        booking.cancelByStudent();
        futurePerf.addBooking(booking);

        assertFalse(futurePerf.checkBookedPerfByStudent("student@test.com"),
                "should return false when the booking is cancelled");
    }

    @Test
    @DisplayName("should return false when there is no booking")
    void shouldReturnFalseWhenNoBookings() {
        assertFalse(futurePerf.checkBookedPerfByStudent("abc@test.com"),
                "should return false when there is no booking made by student");
    }

    @Test
    @DisplayName("return false when the email is different from the booking ones")
    void returnFalseWhenTheEmailIsDifferentFromTheBookingOnes() {
        Student student = new Student("student@test.com", "pass", "student", 1234567890);
        Booking booking = new Booking(1, 5, 60.00, LocalDateTime.now());
        booking.setStudent(student);
        booking.setPerformance(futurePerf);
        futurePerf.addBooking(booking);

        assertFalse(futurePerf.checkBookedPerfByStudent("abc123@test.com"),
                "should return false when the email is different from the booking ones");
    }

    //review()
    @Test
    @DisplayName("review adds rating to the rating collection")
    void reviewAddsRatingToTheRatingCollection() {
        futurePerf.review(5,"mid");
        Collection<Integer> ratings = futurePerf.getReviewRatings();
        assertTrue(ratings.contains(5),
                "5 should be added to the collection");
    }

    @Test
    @DisplayName("review adds comments to the comments collection")
    void reviewAddsCommentsToTheCommentsCollection() {
        futurePerf.review(5,"mid");
        Collection<String> comments = futurePerf.getReviewComments();
        assertTrue(comments.contains("mid"), "mid should be added to the collection");
    }

    @Test
    @DisplayName("review with only rating and no comment have to be added to the collection")
    void reviewWithOnlyRatingAndNoCommentHaveToBeAddedToTheCollection() {
        futurePerf.review(5,"");
        Collection<Integer> ratings = futurePerf.getReviewRatings();
        assertTrue(ratings.contains(5),
                "5 should be added to the collection");
    }

    @Test
    @DisplayName("review with no comment have to be added to the comment collection as well")
    void reviewWithNoCommentHaveToBeAddedToTheCollection() {
        futurePerf.review(5,"");
        Collection<String> comments = futurePerf.getReviewComments();
        assertTrue(comments.contains(""), "empty string should be added to the collection");
    }

    @Test
    @DisplayName("review with the lowest rating is acceptable")
    void reviewWithLowestRatingIsAcceptable() {
        futurePerf.review(1,":)");
        Collection<Integer> ratings = futurePerf.getReviewRatings();
        assertTrue(ratings.contains(1), "1 should be added to the collection");
    }

    @Test
    @DisplayName("review with the highest rating 10 is acceptable")
    void reviewWithHighestRatingIsAcceptable() {
        futurePerf.review(10,":)");
        Collection<Integer> ratings = futurePerf.getReviewRatings();
        assertTrue(ratings.contains(10), "10 should be added to the collection");
    }

    @Test
    @DisplayName("multiple review should be stored correctly")
    void multipleReviewsShouldBeStoredCorrectly() {
        futurePerf.review(1,":)");
        futurePerf.review(10,":)");
        Collection<Integer> ratings = futurePerf.getReviewRatings();
        assertEquals(2, ratings.size(), "there must be 2 reviews stored ");
    }









}
