package eventApp.controller;

import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;

class EditPreferencesSystemTests extends SystemInitialisation{

    // SUCCESS CASES
    // CASE 1 student successfully enters all 3 preferences

    @Test
    @DisplayName("Student successfully edits 3 preferences")
    void testStudentsEditsAllPreferencesSuccess(){
        TestHelper.loginAsStudent(userController, view);

        when(view.getInput("Enter preference 1 (MUSIC/THEATRE/DANCE/MOVIE/SPORTS)" +
                ": ")).thenReturn("MUSIC");
        when(view.getInput("Enter preference 2 (MUSIC/THEATRE/DANCE/MOVIE/SPORTS) " +
                "or 'done' to finish: ")).thenReturn("DANCE");
        when(view.getInput("Enter preference 3 (MUSIC/THEATRE/DANCE/MOVIE/SPORTS) " +
                "or 'done' to finish: ")).thenReturn("SPORTS");
        when(view.getInput("Press ENTER to return to dashboard... \n")).thenReturn("");

        userController.editPreferences();

        // verify success
        verify(view).displaySuccess("Preferences update successfully!");
    }

    // CASE 2 student enters less than 3 preferences
    @Test
    @DisplayName("Student successfully edits less than 3 preferences")
    void testStudentsEditsUnder3PreferencesSuccess(){
        TestHelper.loginAsStudent(userController, view);

        when(view.getInput("Enter preference 1 (MUSIC/THEATRE/DANCE/MOVIE/SPORTS)" +
                ": ")).thenReturn("MUSIC");
        when(view.getInput("Enter preference 2 (MUSIC/THEATRE/DANCE/MOVIE/SPORTS) " +
                "or 'done' to finish: ")).thenReturn("done");
        when(view.getInput("Press ENTER to return to dashboard... \n")).thenReturn("");

        userController.editPreferences();

        // verify success
        verify(view).displaySuccess("Preferences update successfully!");
    }

    // FAILURE CASES
    // CASE 1 user is not a student, i.e. EP, admin, or guest
    @Test
    @DisplayName("Non student tries to edit preferences")
    void testNonStudentEditPreferencesFail(){
        TestHelper.loginAsAdmin(userController, view);

        userController.editPreferences();

        // verify failure
        verify(view).displayError("Only student can edit the preferences!");
    }

    // CASE 2 student enters an invalid preference type
    @Test
    @DisplayName("Edit fails, invalid preferences")
    void testStudentEditPreferenceFailureInvalidPreferences(){
        TestHelper.loginAsStudent(userController, view);

        when(view.getInput("Enter preference 1 (MUSIC/THEATRE/DANCE/MOVIE/SPORTS)" +
                ": ")).thenReturn("COMEDY").thenReturn("MUSIC");
        when(view.getInput("Enter preference 2 (MUSIC/THEATRE/DANCE/MOVIE/SPORTS) " +
                "or 'done' to finish: ")).thenReturn("DANCE");
        when(view.getInput("Enter preference 3 (MUSIC/THEATRE/DANCE/MOVIE/SPORTS) " +
                "or 'done' to finish: ")).thenReturn("SPORTS");
        when(view.getInput("Press ENTER to return to dashboard... \n")).thenReturn("");

        userController.editPreferences();

        // verify error is caught
        verify(view).displayError("Invalid preference! Please try again and use: " +
                        "MUSIC, THEATRE, DANCE, MOVIE, SPORTS");
        // verify success
        verify(view).displaySuccess("Preferences update successfully!");
    }

    // CASE 3 students selects duplicate preferences
    @Test
    @DisplayName("Edit fails, duplicate preferences")
    void testStudentEditPreferenceFailureDuplicatePreferences(){
        TestHelper.loginAsStudent(userController, view);

        when(view.getInput("Enter preference 1 (MUSIC/THEATRE/DANCE/MOVIE/SPORTS)" +
                ": ")).thenReturn("COMEDY").thenReturn("MUSIC");
        when(view.getInput("Enter preference 2 (MUSIC/THEATRE/DANCE/MOVIE/SPORTS) " +
                "or 'done' to finish: ")).thenReturn("MUSIC").thenReturn("DANCE");
        when(view.getInput("Enter preference 3 (MUSIC/THEATRE/DANCE/MOVIE/SPORTS) " +
                "or 'done' to finish: ")).thenReturn("SPORTS");
        when(view.getInput("Press ENTER to return to dashboard... \n")).thenReturn("");

        userController.editPreferences();

        // verify error is caught
        verify(view).displayError("You have already selected MUSIC!");
        // verify success
        verify(view).displaySuccess("Preferences update successfully!");
    }

    // CASE 4 students enters no preferences
    @Test
    @DisplayName("Edit fails, duplicate preferences")
    void testStudentEditPreferenceFailureEmptyPreferences(){
        TestHelper.loginAsStudent(userController, view);

        when(view.getInput("Enter preference 1 (MUSIC/THEATRE/DANCE/MOVIE/SPORTS)" +
                ": ")).thenReturn("done").thenReturn("MUSIC");
        when(view.getInput("Enter preference 2 (MUSIC/THEATRE/DANCE/MOVIE/SPORTS) " +
                "or 'done' to finish: ")).thenReturn("done");
        when(view.getInput("Press ENTER to return to dashboard... \n")).thenReturn("");

        userController.editPreferences();

        // verify error is caught
        verify(view).displayError("Please select at least one preference.");
        // verify success
        verify(view).displaySuccess("Preferences update successfully!");
    }

    // ERROR input is null
    // FIX typo MUSIC instead of MOVIE in the getInput
}