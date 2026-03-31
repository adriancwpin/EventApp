package eventApp.controller;


import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;


class LogoutSystemTests extends SystemInitialisation{

    // SUCCESS CASES
    // first log in student before logging out

    @Test
    @DisplayName("Student logouts successfully")
    void testStudentLogoutWhenLoggedIn(){
        TestHelper.loginAsStudent(userController, view);

        userController.logout();

        assertNull(userController.getCurrentUser(), "currentUser should be null after logout");
        assertTrue(userController.checkCurrentUserIsGuest(), "user should be a guest after logout");
    }

    // ADMIN
    @Test
    @DisplayName("Admin logouts successfully")
    void testAdminLogoutWhenLoggedIn(){
        TestHelper.loginAsAdmin(userController, view);

        userController.logout();

        assertNull(userController.getCurrentUser(), "currentUser should be null after logout");
        assertTrue(userController.checkCurrentUserIsGuest(), "user should be a guest after logout");
    }

    // EP
    @Test
    @DisplayName("EP logouts successfully")
    void testEPLogoutWhenLoggedIn(){
        TestHelper.loginAsEP(userController, view, verificationService);

        userController.logout();

        assertNull(userController.getCurrentUser(), "currentUser should be null after logout");
        assertTrue(userController.checkCurrentUserIsGuest(), "user should be a guest after logout");
    }

    // FAILURE CASES
    // when user is not logged in
    @Test
    @DisplayName("Logout fails when user is not logged in")
    void testLogoutWhenNotLoggedIn(){
        // current user is initially null
        userController.logout();

        assertNull(userController.getCurrentUser(), "CurrentUser should stay as null");
        assertTrue(userController.checkCurrentUserIsGuest(), "user should still be a guest");
    }
}