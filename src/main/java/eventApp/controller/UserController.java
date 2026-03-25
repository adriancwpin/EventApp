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
    private Collection<User> user;
    private VerificationService verificationService;

    public UserController(Collection<User> user,VerificationService verificationService) {
        this.user = user;
        this.verificationService = verificationService;
        adadPreregisteredUsers();
    }

    //Getters
    public User getCurrentUser() {
        return currentUser;
    }


    //Methods
    public void login() {}

    public void logout() {}

    public void registerEntertainmentProvider() {}

    private boolean EPAccountAlreadyExists(String email, String orgName, String businessNumber) {
        return false;
    }

    public void editPreferences(){}

    private void addUser(User user) {}

    private void adadPreregisteredUsers() {}

    private EntertainmentProvider getEntertainmentProviderOwningEvent(long eventNumber) {
        return null;
    }

}
