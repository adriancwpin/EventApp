package eventApp.controller;

import eventApp.external.MockPaymentSystem;
import eventApp.external.MockVerificationService;
import eventApp.external.PaymentSystem;
import eventApp.external.VerificationService;
import eventApp.model.*;
import eventApp.view.View;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.Collection;

import static org.mockito.Mockito.mock;

public abstract class SystemInitialisation {
    protected UserController userController;
    protected EventPerformanceController eventPerformanceController;
    protected BookingController bookingController;

    protected Collection<User> users;
    protected Collection<Event> events;
    protected Collection<Performance> performances;
    protected Collection<Booking> bookings;

    protected View view;
    protected VerificationService verificationService;
    protected PaymentSystem paymentSystem;

    @BeforeEach
    void setup(){
        users = new ArrayList<>();
        events = new ArrayList<>();
        performances = new ArrayList<>();
        bookings = new ArrayList<>();

        // create a mock version of view
        view = mock(View.class);
        verificationService = new MockVerificationService();
        paymentSystem = new MockPaymentSystem();
        userController = new UserController(users, view, verificationService);
        userController.setCurrentUser(null);
        eventPerformanceController = new EventPerformanceController(events, performances, view, paymentSystem);
        bookingController = new BookingController(bookings, performances, view, paymentSystem);
    }
}
