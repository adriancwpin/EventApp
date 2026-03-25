package eventApp.controller;

import eventApp.external.PaymentSystem;
import eventApp.model.*;
import eventApp.view.*;
import java.util.Collection;


public class BookingController {
    private long nextBookingNumber;

    private Collection<Booking> bookings;
    private Collection<Performance> performances;
    private User currentUser;
    private View view;
    private PaymentSystem paymentSystem;

    //Constructor
    public BookingController(Collection<Booking> bookings, Collection<Performance> performances,
                             User currentUser, View view, PaymentSystem paymentSystem) {
        this.nextBookingNumber = 1;
        this.bookings = bookings;
        this.performances = performances;
        this.currentUser = currentUser;
        this.view = view;
        this.paymentSystem = paymentSystem;
    }

    //getters
    public User getCurrentUser() {
        return currentUser;
    }

    //Methods
    public void bookPerformance(){}

    public void reviewPerformance(){}

    public void cancelBooking(){}

    private void addBooking(Booking b){}

    private void getPerformanceByID(long performanceID){}

    private boolean checkIfBookingPossible(Performance performance, int numTickets){
        return false;
    }

    private Collection<Booking> findBookingsByEventID(long eventID){
        return null;
    }

    private Booking getBookingByNumber(long bookingNumber){
        return null;
    }
}
