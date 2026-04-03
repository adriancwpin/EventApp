package eventApp.controller;

import eventApp.external.PaymentSystem;
import eventApp.model.*;
import eventApp.external.VerificationService;
import eventApp.view.View;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SponsorPerformanceSystemTests extends SystemInitialisation{
    // first EP must register, login and create an event with a performance

    // SUCCESS CASE

    @Test
    @DisplayName("Admin sponsors performance success")
    void testAdminSponsorsPerformanceSuccess(){
        TestHelper.loginAsEP(userController, view, verificationService);
        TestHelper.createTestEvent(eventPerformanceController, view);

        // now performance is created admin can sponsor performance
        TestHelper.loginAsAdmin(userController, view);
        when(view.getInput("Enter Performance ID (or '-1' to return back to dashboard.)" +
                ": ")).thenReturn("1");
        when(view.getInput("Enter sponsorship amount (or '-1' to return back to dashboard.)" +
                ": £")).thenReturn("5");
        when(view.getInput("Press ENTER to return to dashboard... \n")).thenReturn("");

        eventPerformanceController.sponsorPerformance();

        // verify success
        Performance performance = eventPerformanceController.getPerformances().iterator().next();
        assertTrue(performance.isSponsored());
        assertEquals(5, performance.getSponsoredAmount());

        verify(view).displaySuccess("Sponsorship Successful.");
    }

    // FAILURE CASES

    // CASE 1 user trying to sponsor performance is not an admin
    @Test
    @DisplayName("Non Admin cannot sponsor performance")
    void testNonAdminCannotSponsorsPerformanceFailure(){
        TestHelper.loginAsEP(userController, view, verificationService);
        TestHelper.createTestEvent(eventPerformanceController, view);

        // now performance is created admin can sponsor performance
        TestHelper.loginAsStudent(userController, view);

        eventPerformanceController.sponsorPerformance();

        // verify error is caught
        Performance performance = eventPerformanceController.getPerformances().iterator().next();
        assertFalse(performance.isSponsored());
        assertFalse(userController.checkCurrentUserIsAdmin());

        verify(view).displayError("Only Admin can sponsor performance.");
    }

    // CASE 2 performance ID is not found
    @Test
    @DisplayName("Performance is not found")
    void testAdminSponsorsPerformanceFailure_PerformanceNotFound(){
        TestHelper.loginAsEP(userController, view, verificationService);
        TestHelper.createTestEvent(eventPerformanceController, view);

        // now performance is created admin can sponsor performance
        TestHelper.loginAsAdmin(userController, view);
        when(view.getInput("Enter Performance ID (or '-1' to return back to dashboard.)" +
                ": ")).thenReturn("100").thenReturn("-1");
        when(view.getInput("Enter sponsorship amount (or '-1' to return back to dashboard.)" +
                ": £")).thenReturn("5");

        eventPerformanceController.sponsorPerformance();

        // verify error is caught
        Performance performance = eventPerformanceController.getPerformances().iterator().next();
        assertFalse(performance.isSponsored());

        verify(view).displayError("Performance with given number does not exists.");
    }

    // CASE 3 ID entered is not a number

    @Test
    @DisplayName("Performance ID is not a number")
    void testAdminSponsorsPerformanceFailure_InvalidID(){
        TestHelper.loginAsEP(userController, view, verificationService);
        TestHelper.createTestEvent(eventPerformanceController, view);

        // now performance is created admin can sponsor performance
        TestHelper.loginAsAdmin(userController, view);
        when(view.getInput("Enter Performance ID (or '-1' to return back to dashboard.)" +
                ": ")).thenReturn("abc").thenReturn("-1");
        when(view.getInput("Enter sponsorship amount (or '-1' to return back to dashboard.)" +
                ": £")).thenReturn("5");

        eventPerformanceController.sponsorPerformance();

        // verify success
        Performance performance = eventPerformanceController.getPerformances().iterator().next();
        assertFalse(performance.isSponsored());

        verify(view).displayError("Invalid input, please enter a number.");
    }

    // EARLY RETURN CASE performance ID -1
    @Test
    @DisplayName("Sponsor performance early return/exit case Performance ID")
    void testAdminSponsorsPerformanceEarlyReturnPerformanceID(){
        TestHelper.loginAsEP(userController, view, verificationService);
        TestHelper.createTestEvent(eventPerformanceController, view);

        // now performance is created admin can sponsor performance
        TestHelper.loginAsAdmin(userController, view);
        when(view.getInput("Enter Performance ID (or '-1' to return back to dashboard.)" +
                ": ")).thenReturn("-1");
        when(view.getInput("Enter sponsorship amount (or '-1' to return back to dashboard.)" +
                ": £")).thenReturn("5");

        eventPerformanceController.sponsorPerformance();

        // verify success
        Performance performance = eventPerformanceController.getPerformances().iterator().next();
        assertFalse(performance.isSponsored());

        verify(view, never()).displaySuccess("Sponsorship Successful.");
    }

    // EARLY RETURN CASE Sponsorship amount -1
    @Test
    @DisplayName("Sponsor performance early return/exit case Sponsorship Amount")
    void testAdminSponsorsPerformanceEarlyReturnSponsorshipAmount(){
        TestHelper.loginAsEP(userController, view, verificationService);
        TestHelper.createTestEvent(eventPerformanceController, view);

        // now performance is created admin can sponsor performance
        TestHelper.loginAsAdmin(userController, view);
        when(view.getInput("Enter Performance ID (or '-1' to return back to dashboard.)" +
                ": ")).thenReturn("1");
        when(view.getInput("Enter sponsorship amount (or '-1' to return back to dashboard.)" +
                ": £")).thenReturn("-1");

        eventPerformanceController.sponsorPerformance();

        // verify success
        Performance performance = eventPerformanceController.getPerformances().iterator().next();
        assertFalse(performance.isSponsored());

        verify(view, never()).displaySuccess("Sponsorship Successful.");
    }
}