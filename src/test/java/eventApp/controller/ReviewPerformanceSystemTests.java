package eventApp.controller;

import eventApp.enums.BookingStatus;
import eventApp.external.PaymentSystem;
import eventApp.model.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewPerformanceSystemTests extends SystemInitialisation{

    // To review a performance first a registered EP must create an event
    // Student then must have booked the performance
    // and the performance must have already happened
    // unrealistic but, nowhere in the requirements does it specify
    // you can't book a performance that has already happened

    // SUCCESS CASES
    // CASE 1 student enters a comment
    @Test
    @DisplayName("Student reviews performance successfully, leaving a comment")
    void testReviewPerformanceSuccess_withComment(){
        TestHelper.loginAsEP(userController, view, verificationService);
        Event event = TestHelper.createTestEventInPast(eventPerformanceController, view);

        TestHelper.loginAsStudent(userController,view);
        TestHelper.bookTestPerformance(bookingController, view, paymentSystem);

        when(view.getInput("Enter Performance ID to review " +
                "(or '-1' to return back to dashboard): ")).thenReturn("1");
        when(view.getInput("Enter rating (1-10): ")).thenReturn("7");
        when(view.getInput("Comment on the performance (press enter to skip)" +
                ":  ")).thenReturn("It was good!");
        when(view.getInput("Press ENTER to return to dashboard... \n")).thenReturn("");

        bookingController.reviewPerformance();

        // verify success, and rating and comment were added
        Performance performance = eventPerformanceController.getPerformances().iterator().next();

        assertFalse(performance.getReviewRatings().isEmpty());
        assertEquals(7, performance.getReviewRatings().iterator().next());

        assertFalse(performance.getReviewComments().isEmpty());
        assertEquals("It was good!", performance.getReviewComments().iterator().next());

        verify(view).displaySuccess("Review submitted successfully!");
    }

    // CASE 2 student does not leave a comment
    @Test
    @DisplayName("Student reviews performance successfully, without a comment")
    void testReviewPerformanceSuccess_WithoutComment(){
        TestHelper.loginAsEP(userController, view, verificationService);
        Event event = TestHelper.createTestEventInPast(eventPerformanceController, view);

        TestHelper.loginAsStudent(userController,view);
        TestHelper.bookTestPerformance(bookingController, view, paymentSystem);

        when(view.getInput("Enter Performance ID to review " +
                "(or '-1' to return back to dashboard): ")).thenReturn("1");
        when(view.getInput("Enter rating (1-10): ")).thenReturn("7");
        when(view.getInput("Comment on the performance (press enter to skip)" +
                ":  ")).thenReturn("");
        when(view.getInput("Press ENTER to return to dashboard... \n")).thenReturn("");

        bookingController.reviewPerformance();

        // verify success, and rating and comment were added
        Performance performance = eventPerformanceController.getPerformances().iterator().next();

        assertFalse(performance.getReviewRatings().isEmpty());
        assertEquals(7, performance.getReviewRatings().iterator().next());

        verify(view).displaySuccess("Review submitted successfully!");
    }

    // FAILURE CASES

    // CASE 1 user is not a student
    @Test
    @DisplayName("Non Student fails to review performance")
    void testReviewPerformanceFailure_NonStudent(){
        TestHelper.loginAsEP(userController, view, verificationService);
        Event event = TestHelper.createTestEventInPast(eventPerformanceController, view);

        TestHelper.loginAsStudent(userController,view);
        TestHelper.bookTestPerformance(bookingController, view, paymentSystem);

        TestHelper.loginAsAdmin(userController, view);
        bookingController.reviewPerformance();

        // verify error, and no rating or comment were added
        Performance performance = eventPerformanceController.getPerformances().iterator().next();

        assertTrue(performance.getReviewRatings().isEmpty());
        assertTrue(performance.getReviewComments().isEmpty());

        verify(view).displayError("Only student can review performances.");
    }

    // CASE 2 performance with entered performanceID not found
    @Test
    @DisplayName("Student fails to review performance, performanceID not found")
    void testReviewPerformanceFailure_IDDoesntExist(){
        TestHelper.loginAsEP(userController, view, verificationService);
        Event event = TestHelper.createTestEventInPast(eventPerformanceController, view);

        TestHelper.loginAsStudent(userController,view);
        TestHelper.bookTestPerformance(bookingController, view, paymentSystem);

        when(view.getInput("Enter Performance ID to review " +
                "(or '-1' to return back to dashboard): ")).thenReturn("100").thenReturn("1");
        when(view.getInput("Enter rating (1-10): ")).thenReturn("7");
        when(view.getInput("Comment on the performance (press enter to skip)" +
                ":  ")).thenReturn("");
        when(view.getInput("Press ENTER to return to dashboard... \n")).thenReturn("");

        bookingController.reviewPerformance();

        // verify error was caught
        verify(view).displayError("Couldn't find any performance with this performance ID.");

        // verify success, and rating and comment were added after fix
        Performance performance = eventPerformanceController.getPerformances().iterator().next();

        assertFalse(performance.getReviewRatings().isEmpty());
        assertEquals(7, performance.getReviewRatings().iterator().next());

        verify(view).displaySuccess("Review submitted successfully!");
    }

    // CASE 3 performance has not taken place
    @Test
    @DisplayName("Student fails to review performance, performance has not taken place")
    void testReviewPerformanceFailure_NotTakenPlace(){
        TestHelper.loginAsEP(userController, view, verificationService);
        Event eventFuture = TestHelper.createTestEvent(eventPerformanceController, view);

        TestHelper.loginAsStudent(userController,view);
        TestHelper.bookTestPerformance(bookingController, view, paymentSystem);

        when(view.getInput("Enter Performance ID to review " +
                "(or '-1' to return back to dashboard): ")).thenReturn("1").thenReturn("-1");

        bookingController.reviewPerformance();

        // verify error was caught
        verify(view).displayError("You can only review the performance " +
                "that has already taken place.");

        Performance performance = eventPerformanceController.getPerformances().iterator().next();

        assertTrue(performance.getReviewRatings().isEmpty());
        assertTrue(performance.getReviewComments().isEmpty());
    }

    // CASE 4 student tries to review performance they didn't book
    @Test
    @DisplayName("Student fails to review performance, they never booked it")
    void testReviewPerformanceFailure_NeverBooked(){
        TestHelper.loginAsEP(userController, view, verificationService);
        Event event = TestHelper.createTestEventInPast(eventPerformanceController, view);

        TestHelper.loginAsStudent(userController,view);

        when(view.getInput("Enter Performance ID to review " +
                "(or '-1' to return back to dashboard): ")).thenReturn("1").thenReturn("-1");

        bookingController.reviewPerformance();

        // verify error, and rating and comment were not added
        Performance performance = eventPerformanceController.getPerformances().iterator().next();

        assertTrue(performance.getReviewRatings().isEmpty());
        assertTrue(performance.getReviewComments().isEmpty());
        verify(view).displayError("Only student that booked the " +
                "performance is eligible to review.");
    }

    // CASE 5 student doesn't enter a number
    @Test
    @DisplayName("Student fails to review performance, invalid input")
    void testReviewPerformanceFailure_InvalidInput() {
        TestHelper.loginAsEP(userController, view, verificationService);
        Event event = TestHelper.createTestEventInPast(eventPerformanceController, view);

        TestHelper.loginAsStudent(userController, view);
        TestHelper.bookTestPerformance(bookingController, view, paymentSystem);

        when(view.getInput("Enter Performance ID to review " +
                "(or '-1' to return back to dashboard): ")).thenReturn("abc").thenReturn("-1");

        bookingController.reviewPerformance();

        // verify error was caught
        Performance performance = eventPerformanceController.getPerformances().iterator().next();

        assertTrue(performance.getReviewRatings().isEmpty());
        assertTrue(performance.getReviewComments().isEmpty());
        verify(view).displayError("Invalid input. Please enter a number.");
    }

    // CASE 6 student enters a rating outside the range
    @Test
    @DisplayName("Student fails to review performance, rating outside the range")
    void testReviewPerformanceFailure_RatingOutsideRange(){
        TestHelper.loginAsEP(userController, view, verificationService);
        Event event = TestHelper.createTestEventInPast(eventPerformanceController, view);

        TestHelper.loginAsStudent(userController,view);
        TestHelper.bookTestPerformance(bookingController, view, paymentSystem);

        when(view.getInput("Enter Performance ID to review " +
                "(or '-1' to return back to dashboard): ")).thenReturn("1");
        when(view.getInput("Enter rating (1-10): ")).thenReturn("75").thenReturn("7");
        when(view.getInput("Comment on the performance (press enter to skip)" +
                ":  ")).thenReturn("");
        when(view.getInput("Press ENTER to return to dashboard... \n")).thenReturn("");

        bookingController.reviewPerformance();

        // verify error was caught
        verify(view).displayError("Rating must be between 1 and 10");

        // verify success, and rating and comment were added after fix
        Performance performance = eventPerformanceController.getPerformances().iterator().next();

        assertFalse(performance.getReviewRatings().isEmpty());
        assertEquals(7, performance.getReviewRatings().iterator().next());

        verify(view).displaySuccess("Review submitted successfully!");
    }

    // CASE 7 student enters a rating that isn't a number
    @Test
    @DisplayName("Student fails to review performance, rating isn't a number")
    void testReviewPerformanceFailure_RatingNotANumber(){
        TestHelper.loginAsEP(userController, view, verificationService);
        Event event = TestHelper.createTestEventInPast(eventPerformanceController, view);

        TestHelper.loginAsStudent(userController,view);
        TestHelper.bookTestPerformance(bookingController, view, paymentSystem);

        when(view.getInput("Enter Performance ID to review " +
                "(or '-1' to return back to dashboard): ")).thenReturn("1");
        when(view.getInput("Enter rating (1-10): ")).thenReturn("abc").thenReturn("7");
        when(view.getInput("Comment on the performance (press enter to skip)" +
                ":  ")).thenReturn("");
        when(view.getInput("Press ENTER to return to dashboard... \n")).thenReturn("");

        bookingController.reviewPerformance();

        // verify error was caught
        verify(view).displayError("Invalid input. Please enter a number.");

        // verify success, and rating and comment were added after fix
        Performance performance = eventPerformanceController.getPerformances().iterator().next();

        assertFalse(performance.getReviewRatings().isEmpty());
        assertEquals(7, performance.getReviewRatings().iterator().next());

        verify(view).displaySuccess("Review submitted successfully!");
    }

    // EARLY RETURN CASE student enters -1 to return/exit
    @Test
    @DisplayName("Student exits review performance early")
    void testReviewPerformanceEarlyReturn(){
        TestHelper.loginAsEP(userController, view, verificationService);
        Event event = TestHelper.createTestEventInPast(eventPerformanceController, view);

        TestHelper.loginAsStudent(userController,view);
        TestHelper.bookTestPerformance(bookingController, view, paymentSystem);

        when(view.getInput("Enter Performance ID to review " +
                "(or '-1' to return back to dashboard): ")).thenReturn("-1");

        bookingController.reviewPerformance();

        // verify early return, and rating and comment were not added
        Performance performance = eventPerformanceController.getPerformances().iterator().next();

        assertTrue(performance.getReviewRatings().isEmpty());
        assertTrue(performance.getReviewComments().isEmpty());

        verify(view, never()).displaySuccess("Review submitted successfully!");
    }

    // EDGE CASES
    // CASE 1 rating is 1
    @Test
    @DisplayName("Edge case student reviews performance with rating 1")
    void testReviewPerformanceEdgeCaseRating1(){
        TestHelper.loginAsEP(userController, view, verificationService);
        Event event = TestHelper.createTestEventInPast(eventPerformanceController, view);

        TestHelper.loginAsStudent(userController,view);
        TestHelper.bookTestPerformance(bookingController, view, paymentSystem);

        when(view.getInput("Enter Performance ID to review " +
                "(or '-1' to return back to dashboard): ")).thenReturn("1");
        when(view.getInput("Enter rating (1-10): ")).thenReturn("1");
        when(view.getInput("Comment on the performance (press enter to skip)" +
                ":  ")).thenReturn("");
        when(view.getInput("Press ENTER to return to dashboard... \n")).thenReturn("");

        bookingController.reviewPerformance();

        // verify success, and rating and comment were added
        Performance performance = eventPerformanceController.getPerformances().iterator().next();

        assertFalse(performance.getReviewRatings().isEmpty());
        assertEquals(1, performance.getReviewRatings().iterator().next());

        verify(view).displaySuccess("Review submitted successfully!");
    }

    // CASE 2 rating is 10
    @Test
    @DisplayName("Edge case student reviews performance with rating 10")
    void testReviewPerformanceEdgeCaseRating10(){
        TestHelper.loginAsEP(userController, view, verificationService);
        Event event = TestHelper.createTestEventInPast(eventPerformanceController, view);

        TestHelper.loginAsStudent(userController,view);
        TestHelper.bookTestPerformance(bookingController, view, paymentSystem);

        when(view.getInput("Enter Performance ID to review " +
                "(or '-1' to return back to dashboard): ")).thenReturn("1");
        when(view.getInput("Enter rating (1-10): ")).thenReturn("10");
        when(view.getInput("Comment on the performance (press enter to skip)" +
                ":  ")).thenReturn("");
        when(view.getInput("Press ENTER to return to dashboard... \n")).thenReturn("");

        bookingController.reviewPerformance();

        // verify success, and rating and comment were added
        Performance performance = eventPerformanceController.getPerformances().iterator().next();

        assertFalse(performance.getReviewRatings().isEmpty());
        assertEquals(10, performance.getReviewRatings().iterator().next());

        verify(view).displaySuccess("Review submitted successfully!");
    }
}