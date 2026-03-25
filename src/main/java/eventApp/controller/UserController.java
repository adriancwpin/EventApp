package eventApp.controller;

import eventApp.model.*;
import eventApp.external.*;
import eventApp.view.*;
import java.util.*;

/**
 * handles all user-related operations including login,logout
 * and registration of Entertainment Providers
 */

public class UserController extends Controller {
    //file path constant
    public static final String PREREGISTERED_USERS_FILE_PATH = "resources/preregistered_users.txt";
    public static final String PREREGISTERED_ADMIN_FILE_PATH = "resources/preregistered_admin.txt";

    //shared resources based on the dependancy on class diagram
    private Collection<User> users;
    private VerificationService verificationService;

    public UserController(Collection<User> user,View view, VerificationService verificationService) {
        this.users = user;
        this.view = view;
        this.verificationService = verificationService;
        addPreregisteredUsers();
    }

    //Getters
    public User getCurrentUser() {
        return currentUser;
    }


    //Methods

    /**
     * Prompt the users to log them into the system.
     * If a match is found in the user records, the current session user is updated
     * and a success message is displayed. Otherwise, an error is shown.
     */
    public void login() {
        String email = view.getInput("Enter email: ");
        String password = view.getInput("Enter password: ");

        for (User user : users) {
            if(user.getEmail().equals(email) && user.getPassword().equals(password)) {
                this.currentUser = user;
                view.displaySuccess("Welcome to the Event App!");
                return;
            }
        }
        //No match found
        view.displayError("Invalid email or password!");
    }

    /**
     * nobody is logged in when the currentUser is null
     * display success message
     */
    public void logout() {
        if(currentUser != null) {
            view.displayError("No user logged in.");
        }

        //set currentUser to null
        this.currentUser = null;

        view.displaySuccess("You have been logged out successfully.");
    }

    public void registerEntertainmentProvider() {
        //Ask for EP details eg orgName
        String orgName = view.getInput("Enter organisation name: ");
        String businessNumber = view.getInput("Enter business number: ");
        String name = view.getInput("Enter name: ");
        String description = view.getInput("Enter description: ");
        String email = view.getInput("Enter email: ");
        String password = view.getInput("Enter password: ");

        //check if account already exits
        if (EPAccountAlreadyExists(email,orgName, businessNumber)){
            view.displayError("This account already exists!");
            return;
        }

        //verify business number via verification system
        if(!verificationService.verifyEntertainmentProvider(businessNumber)){
            view.displayError("Business number is not verified!");
            return;
        }

        //create new account that new EP
        EntertainmentProvider entertainmentProvider = new EntertainmentProvider(email, password, orgName, businessNumber,
                name, description);

        addUser(entertainmentProvider);

        //display success
        view.displaySuccess("Entertainment Provider registered successfully!");
    }

    private boolean EPAccountAlreadyExists(String email, String orgName, String businessNumber) {
        //Go through every user and check
        for(User user : users) {
            if(user instanceof EntertainmentProvider ep) {
                if(ep.getEmail().equals(email) && ep.getOrgName().equals(orgName)
                && ep.getBusinessNumber().equals(businessNumber)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void editPreferences(){}

    private void addUser(User user) {
        users.add(user);
    }

    private void addPreregisteredUsers() {
        //test log in
        addUser(new Student("John", 012345 , "student@test.com","password123"));
        // test EP
        addUser(new EntertainmentProvider(
                "Music Corp", "BN12345678", "Smith", "We organise music events",
                "ep@test.com", "ep123"
            ));
    }

    private EntertainmentProvider getEntertainmentProviderOwningEvent(long eventNumber) {
        return null;
    }

}
