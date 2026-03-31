package eventApp.controller;

import eventApp.enums.AdminMenuOptions;
import eventApp.enums.EPMenuOptions;
import eventApp.enums.GuestMenuOptions;
import eventApp.enums.StudentMenuOptions;
import eventApp.external.PaymentSystem;
import eventApp.external.VerificationService;
import eventApp.model.*;
import eventApp.view.*;
import java.util.*;



public class MenuController extends Controller {

    //All controllers 1-1 relationship
    private UserController userController;
    private EventPerformanceController eventPerformanceController;
    private BookingController bookingController;

    //Shared collections
    private Collection<User> users;
    private Collection<Event> events;
    private Collection<Booking> bookings;
    private Collection<Performance>performances;

    public MenuController(View view, PaymentSystem paymentSystem, VerificationService verificationService){
        this.view = view;

        //initialise shared collections
        this.users = new ArrayList<>();
        this.events = new ArrayList<>();
        this.bookings = new ArrayList<>();
        this.performances = new ArrayList<>();

        //initialise all controllers and shared resources
        this.userController = new UserController(users,view,verificationService);
        this.eventPerformanceController = new EventPerformanceController(events, performances, view, paymentSystem);
        this.bookingController = new BookingController(bookings, performances,view, paymentSystem);
    }

    /**
     * Keep the app running in a loop
     * Check who is currently logged in
     * Show the right menu based on the user
     * Exit the loop only when the user logged out
     */
    public void mainMenu() {
        boolean running = true;

        while (running) {
            //sync currentUSer from UserController
            currentUser = userController.getCurrentUser();

            eventPerformanceController.setCurrentUser(currentUser);
            bookingController.setCurrentUser(currentUser);

            if (checkCurrentUserIsGuest()){
                running = handleGuestMainMenu();
            } else if (checkCurrentUserIsAdmin()) {
                running = handleAdminStaffMainMenu();
            } else if (checkCurrentUserIsEntertainmentProvider()){
                running = handleEntertainmentProviderMainMenu();
            } else if (checkCurrentUserIsStudent()){
                running = handleStudentMainMenu();
            }
        }
    }


    private boolean handleGuestMainMenu(){
        //get all options from guest enums
        List<GuestMenuOptions>options = new ArrayList<>(Arrays.asList(GuestMenuOptions.values()));

        //display menu and choose the choices
        int choice = selectFromMenu(options, "=== Guest Main Menu ===");

        //action based on the choice
        GuestMenuOptions selected = options.get(choice - 1);

        switch (selected) {
            case LOGIN:
                userController.login();
                break;

            case REGISTER_EP:
                userController.registerEntertainmentProvider();
                break;

        }
        return true;
    }

    private boolean handleStudentMainMenu() {
        while(true) {
            List<StudentMenuOptions> options = new ArrayList<>(Arrays.asList(StudentMenuOptions.values()));

            int choice = selectFromMenu(options, "=== Student Main Menu ===");
            StudentMenuOptions selected = options.get(choice - 1);

            switch (selected) {
                case LOGOUT:
                    userController.logout();
                    return true;

                case SEARCH_FOR_PERFORMANCES:
                    eventPerformanceController.searchForPerformances();
                    break;

                case VIEW_PERFORMANCE:
                    eventPerformanceController.viewPerformance();
                    break;

                case REVIEW_PERFORMANCE:
                    bookingController.reviewPerformance();
                    break;

                case EDIT_PREFERENCES:
                    userController.editPreferences();
                    break;

                case BOOK_EVENT:
                    bookingController.bookPerformance();
                    break;

                case CANCEL_BOOKING:
                    bookingController.cancelBooking();
            }
        }
    }

    private boolean handleEntertainmentProviderMainMenu(){
        while(true) {
            List<EPMenuOptions> options = new ArrayList<>(Arrays.asList(EPMenuOptions.values()));

            int choice = selectFromMenu(options, "=== Entertainment Provider Main Menu ===");
            EPMenuOptions selected = options.get(choice - 1);

            switch (selected) {
                case LOGOUT:
                    userController.logout();
                    return true;

                case SEARCH_FOR_PERFORMANCES:
                    eventPerformanceController.searchForPerformances();
                    break;

                case VIEW_PERFORMANCE:
                    eventPerformanceController.viewPerformance();
                    break;

                case CREATE_EVENT:
                    eventPerformanceController.createEvent();
                    break;

                case CANCEL_PERFORMANCE:
                    eventPerformanceController.cancelPerformance();
                    break;
            }
        }
    }

    private boolean handleAdminStaffMainMenu(){
        while(true) {
            List<AdminMenuOptions> options = new ArrayList<>(Arrays.asList(AdminMenuOptions.values()));
            int choice = selectFromMenu(options, "=== Admin Staff Main Menu ===");
            AdminMenuOptions selected = options.get(choice - 1);

            switch (selected) {
                case LOGOUT:
                    userController.logout();
                    return true;

                case SEARCH_FOR_PERFORMANCES:
                    eventPerformanceController.searchForPerformances();
                    break;

                case VIEW_PERFORMANCE:
                    eventPerformanceController.viewPerformance();
                    break;

                case SPONSOR_PERFORMANCE:
                    eventPerformanceController.sponsorPerformance();
                    break;
            }
        }
    }
}
