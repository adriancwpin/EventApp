package eventApp.controller;

import eventApp.model.*;
import eventApp.external.VerificationService;
import eventApp.view.View;
import java.util.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LogInSystemTests {

    private UserController userController;
    private Collection<User> users;
    private View view;
    private VerificationService verificationService;

    @BeforeEach
    void setup(){
        users = new ArrayList<>();

        // create a mock version of view
        view = mock(View.class);
        verificationService = mock(VerificationService.class);
        userController = new UserController(users, view, null);
        userController.setCurrentUser(null);
    }

    // create tests for student login
    // student details are already pre-registered
    // use mockito to simulate user input

    @Test
    @DisplayName("Student logs in with correct credentials")
    void testStudentLogInSuccess() {
        when(view.getInput("Enter email: ")).thenReturn("student@test.com");
        when(view.getInput("Enter password: ")).thenReturn("password123");

        userController.login();

        assertNotNull(userController.getCurrentUser(), "currentUser should not be null after successful login");
        assertTrue(userController.checkCurrentUserIsStudent(), "logged in user should be a Student");
        assertFalse(userController.checkCurrentUserIsGuest(), "user should no longer be a guest");
    }

    // create tests for admin login
    // admin details are already pre-registered

    @Test
    @DisplayName("Admin logs in with correct credentials")
    void testAdminLoginCorrectCredentials() {
        when(view.getInput("Enter email: ")).thenReturn("admin@test.com");
        when(view.getInput("Enter password: ")).thenReturn("admin123");

        userController.login();

        assertNotNull(userController.getCurrentUser(), "currentUser should not be null after successful admin login");
        assertTrue(userController.checkCurrentUserIsAdmin(), "logged in user should be AdminStaff");
        assertFalse(userController.checkCurrentUserIsGuest());
    }

    @Test
    @DisplayName("Admin login fails with wrong password but correct email")
    void testAdminLoginWrongPasswordCorrectEmail() {
        when(view.getInput("Enter email: ")).thenReturn("admin@test.com");
        when(view.getInput("Enter password: ")).thenReturn("wrongpassword");

        userController.login();

        assertNull(userController.getCurrentUser(), "Wrong password should not log admin in");
        assertFalse(userController.checkCurrentUserIsAdmin());
    }

    @Test
    @DisplayName("Admin login fails with wrong email but correct password")
    void testAdminLoginWrongEmailCorrectPassword() {
        when(view.getInput("Enter email: ")).thenReturn("wrong@email.com");
        when(view.getInput("Enter password: ")).thenReturn("admin123");

        userController.login();

        assertNull(userController.getCurrentUser(), "Wrong email should not log admin in");
        assertFalse(userController.checkCurrentUserIsAdmin());
    }

    // create tests for EP
    // EP must have registered before being able to log in

    private void registerTestEP(String orgName, String businessNumber,
                                 String name, String description,
                                 String email, String password) {
        // return EP details
        when(view.getInput("Enter organisation name: ")).thenReturn(orgName);
        when(view.getInput("Enter business number: ")).thenReturn(businessNumber);
        when(view.getInput("Enter name: ")).thenReturn(name);
        when(view.getInput("Enter description: ")).thenReturn(description);
        when(view.getInput("Enter email: ")).thenReturn(email);
        when(view.getInput("Enter password: ")).thenReturn(password);

        // approve business number
        when(verificationService.verifyEntertainmentProvider(businessNumber)).thenReturn(true);

        userController.registerEntertainmentProvider();

        // Reset currentUser
        userController.setCurrentUser(null);
    }

    @Test
    void testEPLoginCorrectCredentialsAfterRegistration() {
        registerTestEP("Music Corp", "BN12345678", "Smith",
                 "We organise music events", "ep@test.com", "ep123");

        when(view.getInput("Enter email: ")).thenReturn("ep@test.com");
        when(view.getInput("Enter password: ")).thenReturn("ep123");

        userController.login();

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
    @DisplayName(" EP login fails with wrong credentials")
    void testEPLoginWrongCredentialsAfterRegistration() {
        registerTestEP("Music Corp", "BN12345678", "Smith",
                "We organise music events", "ep@test.com", "ep123");

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