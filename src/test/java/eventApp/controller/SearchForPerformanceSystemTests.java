package eventApp.controller;

import eventApp.model.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;

class SearchForPerformanceSystemTests extends SystemInitialisation{
    // SUCCESS CASES
    // CASE 1 student searches for performance
    // Need to register and login EP to first create an event
    @Test
    @DisplayName("Student successfully searches for performances")
    void testSearchForPerformance_StudentSuccess(){
        TestHelper.loginAsEP(userController,view, verificationService);
        Event event = TestHelper.createTestEvent(eventPerformanceController, view);

        // now event is created, student can search for performance
        TestHelper.loginAsStudent(userController, view);
        TestHelper.searchTestPerformances(eventPerformanceController, view);

        // verify success
        verify(view).displayListOfPerformances(any());
    }

    // CASE 2 admin/EP searches for performance
    @Test
    @DisplayName("EP/Admin successfully searches for performances")
    void testSearchForPerformance_EPAdminSuccess(){
        TestHelper.loginAsEP(userController, view, verificationService);
        Event event = TestHelper.createTestEvent(eventPerformanceController, view);

        // now event is created, EP can search for performance
        TestHelper.loginAsEP(userController, view, verificationService);
        TestHelper.searchTestPerformances(eventPerformanceController, view);

        // verify success
        verify(view).displayListOfPerformances(any());
    }

    // FAILURE CASES
    // These will be done as a student
    // CASE 1 invalid date/time format
    @Test
    @DisplayName("User cannot search for performances, invalid date/time format input")
    void testSearchForPerformance_FailureInvalidDateTime(){
        TestHelper.loginAsEP(userController,view, verificationService);
        Event event = TestHelper.createTestEvent(eventPerformanceController, view);

        // now event is created, student can search for performance
        TestHelper.loginAsStudent(userController, view);
        when(view.getInput("\nEnter Search Date of Performance (dd/MM/yyyy): ")).thenReturn("0100/01/20").thenReturn("01/01/2000");
        when(view.getInput("Press ENTER to return to dashboard...\n")).thenReturn("");
        eventPerformanceController.searchForPerformances();

        // verify error is caught
        verify(view).displayError("Invalid date/time format. Please use dd/MM/yyyy format.");

        // verify error is then fixed
        verify(view).displayListOfPerformances(any());
    }

    // CASE 2 no performances are found on the date
    @Test
    @DisplayName("User cannot search for performances, no performances found on the date")
    void testSearchForPerformance_FailureNoPerformancesFound(){
        TestHelper.loginAsEP(userController,view, verificationService);
        Event event = TestHelper.createTestEvent(eventPerformanceController, view);

        // now event is created, student can search for performance
        TestHelper.loginAsStudent(userController, view);
        when(view.getInput("\nEnter Search Date of Performance (dd/MM/yyyy): ")).thenReturn("25/02/2026");
        when(view.getInput("Press ENTER to return to dashboard...\n")).thenReturn("");

        eventPerformanceController.searchForPerformances();

        // verify error is caught
        verify(view).displayError("No performances found on this date!");
    }
}