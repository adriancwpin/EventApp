package eventApp.controller;

import eventApp.external.MockVerificationService;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class RegisterEntertainmentProviderSystemTests extends SystemInitialisation{

    // SUCCESS CASE
    @Test
    @DisplayName("EP successfully registers")
    void testEPRegistrationSuccess(){
        TestHelper.registerTestEP(userController, view, verificationService,
                "Test org", "BN87654321", "John",
                "We organise tests", "newep@test.com", "testep321");

        verify(view).displaySuccess("Entertainment Provider registered successfully!");
    }

    // FAILURE CASES
    // CASE 1 when business number is not verified
    // use a number that is not valid

    @Test
    @DisplayName("Registration fails when business number is not verified")
    void testEPRegistrationFailureBusinessNotVerified(){
        when(view.getInput("Enter organisation name: ")).thenReturn("Test org");
        when(view.getInput("Enter business number: ")).thenReturn("BN1234");
        when(view.getInput("Enter name: ")).thenReturn("John");
        when(view.getInput("Enter description: ")).thenReturn("We organise tests");
        when(view.getInput("Enter email: ")).thenReturn("newep@test.com");
        when(view.getInput("Enter password: ")).thenReturn("testep123");

        userController.registerEntertainmentProvider();

        // verify the error is caught
        verify(view).displayError("Business number is not verified!");
    }

    // Case 2
    // Register once and then try to register again
    @Test
    @DisplayName("Registration fails when account already exists")
    void testEPRegistrationFailureAccountAlreadyExists(){
        TestHelper.registerTestEP(userController, view, verificationService,
                "Test org", "BN87654321", "John",
                "We organise tests", "newep@test.com", "testep321");

        TestHelper.registerTestEP(userController, view, verificationService,
                "Test org", "BN87654321", "John",
                "We organise tests", "newep@test.com", "testep321");

        // Verify the error is caught
        verify(view).displayError("This account already exists!");
    }

    // Case 3
    // email is not valid format
    @Test
    @DisplayName("Registration fails with invalid email")
    void testEPRegistrationFailureEmailInvalid(){
        when(view.getInput("Enter organisation name: ")).thenReturn("Test org");
        when(view.getInput("Enter business number: ")).thenReturn("BN87654321");
        when(view.getInput("Enter name: ")).thenReturn("John");
        when(view.getInput("Enter description: ")).thenReturn("We organise tests");

        // return invalid first, then valid on the second call since its a loop
        when(view.getInput("Enter email: ")).thenReturn("invalidemail.com").thenReturn("newep@test.com");

        userController.registerEntertainmentProvider();

        // verify the error is caught
        verify(view).displayError("Invalid email format! Please use format: example@domain.com");

        // verify the registration is then successful
        verify(view).displaySuccess("Entertainment Provider registered successfully!");
    }
}