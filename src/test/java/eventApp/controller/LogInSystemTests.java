package eventApp.controller;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LogInSystemTests extends SystemInitialisation{

    // create tests for student login
    // student details are already pre-registered
    // use mockito to simulate user input

    @Test
    @DisplayName("Student logs in with correct credentials")
    void testStudentLogInSuccess() {
        TestHelper.loginAsStudent(userController, view);

        assertNotNull(userController.getCurrentUser(), "currentUser should not be null after successful login");
        assertTrue(userController.checkCurrentUserIsStudent(), "logged in user should be a Student");
        assertFalse(userController.checkCurrentUserIsGuest(), "user should no longer be a guest");
    }

    // create tests for admin login
    // admin details are already pre-registered

    @Test
    @DisplayName("Admin logs in with correct credentials")
    void testAdminLoginCorrectCredentials() {
        TestHelper.loginAsAdmin(userController, view);

        assertNotNull(userController.getCurrentUser(), "currentUser should not be null after successful admin login");
        assertTrue(userController.checkCurrentUserIsAdmin(), "logged in user should be AdminStaff");
        assertFalse(userController.checkCurrentUserIsGuest());
    }

    // create tests for EP
    // EP must have registered before being able to log in

    @Test
    void testEPLoginCorrectCredentialsAfterRegistration() {
        TestHelper.loginAsEP(userController, view, verificationService);

        assertNotNull(userController.getCurrentUser(), "EP should be logged in after registration");
        assertTrue(userController.checkCurrentUserIsEntertainmentProvider(),
                "logged in user should be an EntertainmentProvider");
        assertFalse(userController.checkCurrentUserIsGuest());
    }

    // FAILURE CASES

    @Test
    @DisplayName("EP login fails if not yet registered")
    void testEPLoginWithoutRegistering(){
        when(view.getInput("Enter email: ")).thenReturn("epnotregistered@test.com");
        when(view.getInput("Enter password: ")).thenReturn("randompassword");

        userController.login();

        assertNull(userController.getCurrentUser(), "Unregistered EP should not be able to login");
        assertTrue(userController.checkCurrentUserIsGuest());
    }

    // EP failure case
    // Don't need to check all cases when either email or password is incorrect

    @Test
    @DisplayName("EP login fails with wrong credentials")
    void testEPLoginWrongCredentialsAfterRegistration() {
        TestHelper.registerTestEP(userController, view, verificationService,
                "Test org", "BN87654321", "John",
                "We organise tests", "newep@test.com", "testep321");

        when(view.getInput("Enter email: ")).thenReturn("ep@test.com");
        when(view.getInput("Enter password: ")).thenReturn("wrongpassword");

        userController.login();

        assertNull(userController.getCurrentUser(), "Wrong password should not log EP in");
        assertFalse(userController.checkCurrentUserIsEntertainmentProvider());
    }

    // pre-registered users failure case

    @Test
    @DisplayName("Pre-registered user login fails with wrong credentials")
    void testPreRegisteredUserLoginWrongCredentials(){
        when(view.getInput("Enter email: ")).thenReturn("wrong@email.com");
        when(view.getInput("Enter password: ")).thenReturn("wrongpassword");

        userController.login();

        assertNull(userController.getCurrentUser(), "Wrong credentials should not log user in");
        assertTrue(userController.checkCurrentUserIsGuest(), "user should still be a guest");
    }

    // test for password case sensitivity

    @Test
    @DisplayName("User login fails with capitalised password")
    void testLoginCaseSensitive(){
        when(view.getInput("Enter email: ")).thenReturn("student@test.com");
        when(view.getInput("Enter password: ")).thenReturn("PASSWORD123");

        userController.login();

        assertNull(userController.getCurrentUser(), "Upper case password should not log user in");
        assertTrue(userController.checkCurrentUserIsGuest(), "user should still be a guest");
    }
}