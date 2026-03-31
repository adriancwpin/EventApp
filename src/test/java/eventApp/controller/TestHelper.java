package eventApp.controller;

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

        // approve business number
        when(verificationService.verifyEntertainmentProvider(businessNumber)).thenReturn(true);

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
        registerTestEP(userController, view, verificationService,
                "Music Corp", "BN12345678", "Smith",
                "We organise music events", "ep@test.com", "ep123");
        when(view.getInput("Enter email: ")).thenReturn("ep@test.com");
        when(view.getInput("Enter password: ")).thenReturn("ep123");
        userController.login();
    }
}
