package eventApp.controller;

import eventApp.external.PaymentSystem;
import eventApp.external.VerificationService;
import eventApp.model.*;
import eventApp.view.*;
import java.util.ArrayList;


import java.util.ArrayList;
import java.util.Collection;

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

    private View view;
    private PaymentSystem paymentSystem;
    private VerificationService verificationService;

    public MenuController(View view, PaymentSystem paymentSystem, VerificationService verificationService){
        this.view = view;
        this.paymentSystem = paymentSystem;
        this.verificationService = verificationService;

        //initialise shared collections
        this.users = new ArrayList();
        this.events = new ArrayList();
        this.bookings = new ArrayList();
        this.performances = new ArrayList();
        this.currentUser = null; // no one logged in at start

        //initialise all controllers and shared resources
        this.userController = new UserController(users, view, verificationService);
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
            if (checkCurrentUserIsGuest()){
                handleGuestMainMenu();
            } else if (checkCurrentUserIsAdmin()) {
                handleAdminStaffMainMenu();
            } else if (checkCurrentUserIsEntertainmentProvider()){
                handleEntertainmentProviderMainMenu();
            } else if (checkCurrentUserIsStudent()){
                handleStudentMainMenu();
            }
        }
    }

    private boolean handleGuestMainMenu(){
        return false;
    }

    private boolean handleStudentMainMenu(){
        return false;
    }

    private boolean handleEntertainmentProviderMainMenu(){
        return false;
    }

    private boolean handleAdminStaffMainMenu(){
        return false;
    }
}
