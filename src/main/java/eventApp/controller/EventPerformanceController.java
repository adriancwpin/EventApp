package eventApp.controller;

import eventApp.external.PaymentSystem;
import eventApp.model.*;
import eventApp.view.*;
import java.util.Collection;

public class EventPerformanceController extends Controller{
    private long nextEventID;
    private long nextPerformanceID;

    private Collection<Event>events;
    private Collection<Performance> performances;
    private PaymentSystem paymentSystem;

    //Constructor
    public EventPerformanceController(Collection<Event> events, Collection<Performance> performances,View view,
                                      PaymentSystem paymentSystem) {
        this.events = events;
        this.performances = performances;
        this.view = view;
        this.paymentSystem = paymentSystem;
        this.nextEventID = 1;
        this.nextPerformanceID = 1;
    }

    //Getters
    public User getCurentUser(){
        return currentUser;
    }

    //Methods
    public Event createEvent() {
        return null;
    }

    public void searchForPerformances(){}

    public void viewPerformance(){}

    public void cancelPerformance(){}

    private boolean checkIfSponsorshipPossible(Performance performance, int amount){
        return false;
    }

    public void sponsorPerformance(){}

    private void addEvent(Event e){}

    private void addPerformance(Performance p){}

    private void getEventByID(long EventID){}

    private void getEventByTitle(String title){}

    private Performance getPerformanceByID(long PerformanceID){
        return null;
    }
}
