package eventApp.controller;

import eventApp.model.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;

class ViewPerformanceSystemTests extends SystemInitialisation{

    // SUCCESS CASE
    // All logged in users can view performances the same
    // first event must be created, so EP must register and login

    @Test
    @DisplayName("Authenticated user successfully views performance")
    void testViewPerformanceSuccess(){
        TestHelper.loginAsEP(userController, view, verificationService);
        Event event = TestHelper.createTestEvent(eventPerformanceController, view);

        // now user can view performance
        TestHelper.loginAsStudent(userController, view);
        when(view.getInput("\nEnter Performance ID (or '-1' to return): ")).thenReturn("1");
        when(view.getInput("\"Press ENTER to return to dashboard...\\n")).thenReturn("");

        eventPerformanceController.viewPerformance();

        // verify performance info was displayed
        verify(view).displaySpecificPerformance(any());
    }
    // Error wasn't calling viewPerformance()

    // FAILURE CASES
    // CASE 1 performance is null, no matching performanceID
    @Test
    @DisplayName("Authenticated user cannot view performance, no matching performanceID found")
    void testViewPerformanceFailure_IDNotFound(){
        TestHelper.loginAsEP(userController, view, verificationService);
        Event event = TestHelper.createTestEvent(eventPerformanceController, view);

        // now user can view performance
        TestHelper.loginAsStudent(userController, view);
        when(view.getInput("\nEnter Performance ID (or '-1' to return): ")).thenReturn("5").thenReturn("1");
        when(view.getInput("\"Press ENTER to return to dashboard...\\n")).thenReturn("");

        eventPerformanceController.viewPerformance();

        // verify error was caught
        verify(view).displayError("Performance ID not found, please try again.");
        // verify performance info was displayed
        verify(view).displaySpecificPerformance(any());
    }

    // CASE 2 NumberFormatException, invalidID
    @Test
    @DisplayName("Authenticated user cannot view performance, invalid ID")
    void testViewPerformanceFailure_InvalidID(){
        TestHelper.loginAsEP(userController, view, verificationService);
        Event event = TestHelper.createTestEvent(eventPerformanceController, view);

        // now user can view performance
        TestHelper.loginAsStudent(userController, view);
        when(view.getInput("\nEnter Performance ID (or '-1' to return): ")).thenReturn("fail").thenReturn("1");
        when(view.getInput("\"Press ENTER to return to dashboard...\\n")).thenReturn("");

        eventPerformanceController.viewPerformance();

        // verify error was caught
        verify(view).displayError("Invalid ID, please enter a number.");
        // verify performance info was displayed
        verify(view).displaySpecificPerformance(any());
    }

    // EARLY RETURN CASE, user enters -1 to return/exit
    @Test
    @DisplayName("Authenticated user returns -1 to return/exit")
    void testViewPerformanceExit(){
        TestHelper.loginAsEP(userController, view, verificationService);
        Event event = TestHelper.createTestEvent(eventPerformanceController, view);

        // now user can view performance
        TestHelper.loginAsStudent(userController, view);
        when(view.getInput("\nEnter Performance ID (or '-1' to return): ")).thenReturn("-1");

        eventPerformanceController.viewPerformance();

        // verify user exits, so performance info wasn't displayed
        verify(view, never()).displaySpecificPerformance(any());
    }
}