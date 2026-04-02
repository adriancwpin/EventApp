package eventApp.controller;

import eventApp.external.PaymentSystem;
import eventApp.model.*;
import eventApp.external.VerificationService;
import eventApp.view.View;
import java.util.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestHelper {
    public static void registerTestEP(UserController userController, View view,
                                      VerificationService verificationService,
                                      String orgName, String businessNumber,
                                      String name, String description,
                                      String email, String password) {
        // return EP details
        when(view.getInput("Enter organisation name: ")).thenReturn(orgName);
        when(view.getInput("Enter business number: ")).thenReturn(businessNumber);
        when(view.getInput("Enter name: ")).thenReturn(name);
        when(view.getInput("Enter description: ")).thenReturn(description);
        when(view.getInput("Enter email: ")).thenReturn(email);
        when(view.getInput("Enter password: ")).thenReturn(password);

        userController.registerEntertainmentProvider();

        // Reset currentUser
        userController.setCurrentUser(null);
    }

    public static void loginAsStudent(UserController userController, View view){
        when(view.getInput("Enter email: ")).thenReturn("student@test.com");
        when(view.getInput("Enter password: ")).thenReturn("password123");
        userController.login();
    }

    public static void loginAsAdmin(UserController userController, View view){
        when(view.getInput("Enter email: ")).thenReturn("admin@test.com");
        when(view.getInput("Enter password: ")).thenReturn("admin123");
        userController.login();
    }

    public static void loginAsEP(UserController userController, View view,
                                 VerificationService verificationService){
        TestHelper.registerTestEP(userController, view, verificationService,
                "Test org", "BN87654321", "John",
                "We organise tests", "newep@test.com", "testep321");
        when(view.getInput("Enter email: ")).thenReturn("newep@test.com");
        when(view.getInput("Enter password: ")).thenReturn("testep321");
        userController.login();
    }

    public static Event createTestEvent(EventPerformanceController eventPerformanceController, View view){
        when(view.getInput("\nEnter Event Title: ")).thenReturn("Test Concert");
        when(view.getInput("Enter Choice: ")).thenReturn("1");
        when(view.getInput("\nIs this event ticketed? (Yes/No): ")).thenReturn("Yes");
        when(view.getInput("\nNumber of performances: ")).thenReturn("1");
        when(view.getInput("Enter Start Date (dd/MM/yyyy): ")).thenReturn("01/01/3000");
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
        return eventPerformanceController.createEvent();
    }

    public static Event createTestEventInPast(EventPerformanceController eventPerformanceController, View view){
        when(view.getInput("\nEnter Event Title: ")).thenReturn("Test Concert");
        when(view.getInput("Enter Choice: ")).thenReturn("1");
        when(view.getInput("\nIs this event ticketed? (Yes/No): ")).thenReturn("Yes");
        when(view.getInput("\nNumber of performances: ")).thenReturn("1");
        when(view.getInput("Enter Start Date (dd/MM/yyyy): ")).thenReturn("01/01/2000");
        when(view.getInput("Enter Start Time (HH:mm): ")).thenReturn("00:00");
        when(view.getInput("Enter End Date (dd/MM/yyyy): ")).thenReturn("10/10/2000");
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
        return eventPerformanceController.createEvent();
    }

    public static void searchTestPerformances(EventPerformanceController eventPerformanceController, View view){
        when(view.getInput("\nEnter Search Date of Performance (dd/MM/yyyy): ")).thenReturn("01/01/3000");
        when(view.getInput("Press ENTER to return to dashboard...\n")).thenReturn("");
        eventPerformanceController.searchForPerformances();
    }

    public static void bookTestPerformance(BookingController bookingController, View view, PaymentSystem paymentSystem){
        when(view.getInput("Enter Performance ID (or '-1' to return back to dashboard)" +
                ": ")).thenReturn("1");
        when(view.getInput("Enter Number of tickets (or '-1' to return back to dashboard)" +
                ": ")).thenReturn("5");
        when(view.getInput("Press ENTER to return to dashboard...\n")).thenReturn("");
        bookingController.bookPerformance();
    }

    public static void loginAsStudent2(UserController userController, View view){
        when(view.getInput("Enter email: ")).thenReturn("student2@test.com");
        when(view.getInput("Enter password: ")).thenReturn("password999");
        userController.login();
    }

    public static void assertEventCreatedSuccess(Event event
            , String expectedTitle, boolean expectedTicketed){
        assertNotNull(event, "Event should now be created");
        assertEquals(expectedTitle, event.getTitle(), "Event title should match");
        assertEquals(expectedTicketed, event.isTicketed(), "Event should be ticketed");
    }

    public static void assertPreferences(StudentPreferences preferences, boolean music,
                                         boolean theatre, boolean dance, boolean movie, boolean sports){
        assertEquals(music, preferences.preferMusicEvents);
        assertEquals(theatre, preferences.preferTheatreEvents);
        assertEquals(dance, preferences.preferDanceEvents);
        assertEquals(movie, preferences.preferMovieEvents);
        assertEquals(sports, preferences.preferSportsEvents);
    }

}
