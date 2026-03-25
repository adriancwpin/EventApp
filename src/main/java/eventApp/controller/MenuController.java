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

    private User currentUser;

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
        this.userController = new UserController(users, currentUser, view, verificationService);
        this.eventPerformanceController = new EventPerformanceController(events, performances, currentUser, view, paymentSystem);
        this.bookingController = new BookingController(bookings, performances, currentUser, view, paymentSystem);
    }

    public void mainMenu() {}

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
