package eventApp.controller;

import eventApp.model.*;
import eventApp.external.*;
import eventApp.view.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;;


/**
 * handles all user-related operations including login,logout
 * and registration of Entertainment Providers
 */

public class UserController extends Controller {
    //file path constant
    public static final String PREREGISTERED_USERS_FILE_PATH = "src/main/resources/preregistered_user.txt";
    public static final String PREREGISTERED_ADMIN_FILE_PATH = "src/main/resources/preregistered_admin.txt";

    //shared resources based on the dependancy on class diagram
    private Collection<User> users;
    private VerificationService verificationService;

    public UserController(Collection<User> user,View view, VerificationService verificationService) {
        this.users = user;
        this.view = view;
        this.verificationService = verificationService;
        addPreregisteredUsers();// where is ur current user stored
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
        if(currentUser == null) {
            view.displayError("No user logged in.");
            return;
        }

        //set currentUser to null
        this.currentUser = null;

        view.displaySuccess("You have been logged out successfully.");
    }

    /**
     * Register a new Entertainment Provider in the system.
     *
     * Prompts the user for organisation details and validates the email format
     * checks that the account does not exist, and verifies the business number
     * via the external verification service. If passed, then a new account is created and added
     * to the system.
     */
    public void registerEntertainmentProvider() {
        //Ask for EP details eg orgName
        String orgName = view.getInput("Enter organisation name: ");
        String businessNumber = view.getInput("Enter business number: ");
        String name = view.getInput("Enter name: ");
        String description = view.getInput("Enter description: ");
        String email = null;
        while (email == null){
            email = view.getInput("Enter email: ").trim();
            if(!isValidEmail(email)){
                view.displayError("Invalid email format! Please use format: example@domain.com");
                email = null;
            }

        }
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
                if(ep.getEmail().equals(email) || ep.getOrgName().equals(orgName)
                || ep.getBusinessNumber().equals(businessNumber)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Allows a logged-in student to edit their event type preferences
     *
     *Prompt the students to select up to 3 preferred event types from:
     * MUSIC, THEATRE, DANCE, MOVIE, SPORTS. At least one preference must be selected.
     * Duplicated selections and invalid inputs are rejected.
     *
     */
    public void editPreferences(){
        //make sure only student can edit preferences
        if(!checkCurrentUserIsStudent()){
            view.displayError("Only student can edit the preferences!");
            return;
        }

        Student student = (Student) currentUser;

        //update up to 3 preferences for valid event types
        // reset preferences
        StudentPreferences pref = student.getPreferences();

        boolean update = false;

        //loop until something changes
        while(!update){
            //show current preferences
            System.out.println("\n=== Current Preferences ===");
            System.out.println("1. Music: "   + (pref.preferMusicEvents   ? "✓" : ""));
            System.out.println("2. Theatre: " + (pref.preferTheatreEvents ? "✓" : ""));
            System.out.println("3. Dance: "   + (pref.preferDanceEvents   ? "✓" : ""));
            System.out.println("4. Movie: "   + (pref.preferMovieEvents   ? "✓" : ""));
            System.out.println("5. Sports: "  + (pref.preferSportsEvents  ? "✓" : ""));
            System.out.println();

            //asking for new preference
            List<String> selectedPref = new ArrayList<>();

            for (int i = 1; i <= 3; i++) {
                String input;
                if (i == 1) {
                    input = view.getInput("Enter preference " + i +
                            " (MUSIC/THEATRE/DANCE/MOVIE/SPORTS): ");
                } else {
                    input = view.getInput("Enter preference " + i +
                            " (MUSIC/THEATRE/DANCE/MOVIE/SPORTS) or 'done' to finish: ");
                }

                if (input.equalsIgnoreCase("done")) {
                    break;
                }

                //validate preference
                input = input.trim().toUpperCase();
                if (!isValidPreference(input)) {
                    view.displayError("Invalid preference! Please try again and use: " +
                            "MUSIC, THEATRE, DANCE, MOVIE, SPORTS");
                    i--; //ask again
                    continue;
                }

                //in case for duplicates
                if (selectedPref.contains(input)) {
                    view.displayError("You have already selected " + input + "!");
                    i--; // try again
                    continue;
                }
                selectedPref.add(input);
            }

            if (selectedPref.isEmpty()) {
                view.displayError("Please select at least one preference.");
                continue;
            }

            //update preferences
            boolean success = student.getPreferences().updatePreference(String.join(",", selectedPref));

            if (success) {
                update =  true;
                //show updated preferences
                System.out.println("\n=== Updated Preferences ===");
                System.out.println("1. Music: " + (pref.preferMusicEvents ? "✓" : ""));
                System.out.println("2. Theatre: " + (pref.preferTheatreEvents ? "✓" : ""));
                System.out.println("3. Dance: " + (pref.preferDanceEvents ? "✓" : ""));
                System.out.println("4. Movie: " + (pref.preferMovieEvents ? "✓" : ""));
                System.out.println("5. Sports: " + (pref.preferSportsEvents ? "✓" : ""));

                view.displaySuccess("Preferences update successfully!");
            } else view.displayError("Something wrong. Please update the preferences again.");
        }
        view.getInput("Press ENTER to return to dashboard... \n");
    }

    //helper function for edit preference
    private boolean isValidPreference(String preference){
        switch (preference){
            case "MUSIC":
            case "THEATRE":
            case "DANCE":
            case "MOVIE":
            case "SPORTS":
                return true;

            default:
                return false;
        }
    }

    private void addUser(User user) {
        users.add(user);
    }

    private void addPreregisteredUsers() {
        readStudentFromFile();
        readAdminFromFile();


    }

    private void readStudentFromFile() {
        try{
            File file = new File(PREREGISTERED_USERS_FILE_PATH);
            Scanner scanner = new Scanner(file);

            while(scanner.hasNextLine()){
                String line = scanner.nextLine().trim();

                //skip empty line
                while(line.isEmpty()){
                    continue;
                }

                String[] split = line.split(",");

                //validate correct format
                if(split.length != 4){
                    view.displayError("Invalid student format: " + line);
                    continue;
                }

                String email = split[0].trim();
                String password = split[1].trim();
                String name = split[2].trim();
                int phone = Integer.parseInt(split[3].trim());

                addUser(new Student(email, password, name, phone));
            }
            scanner.close();
        }catch(FileNotFoundException e){
            view.displayError("Student file not found: "+ PREREGISTERED_USERS_FILE_PATH);
        }catch(NumberFormatException e){
            view.displayError("Invalid phone number format in student file");
        }
    }

    private void readAdminFromFile() {
        try{
            File file = new File(PREREGISTERED_ADMIN_FILE_PATH);
            Scanner scanner = new Scanner(file);

            while(scanner.hasNextLine()){
                String line = scanner.nextLine().trim();

                while(line.isEmpty()){
                    continue;
                }

                String[] split = line.split(",");
                if(split.length != 3){
                    view.displayError("Invalid admin format: " + line);
                    continue;
                }

                String email = split[0].trim();
                String password = split[1].trim();
                String name = split[2].trim();

                addUser(new AdminStaff(email, password, name));
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            view.displayError("Admin file not found: "+ PREREGISTERED_ADMIN_FILE_PATH);
        }
    }



    private EntertainmentProvider getEntertainmentProviderOwningEvent(long eventNumber) {
        for(User user: users){
            if(user instanceof EntertainmentProvider ep) {
                //check if this EP owns the event
                for(Event event: ep.getEvents()){
                    if(event.getEventID() == eventNumber){
                        return ep; //found the ep
                    }
                }
            }
        }
        return null; //not found
    }

}
