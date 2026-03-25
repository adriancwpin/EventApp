package eventApp.controller;

import eventApp.enums.EventType;
import eventApp.external.PaymentSystem;
import eventApp.model.*;
import eventApp.view.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

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

    //Methods
    public Event createEvent() {
        //Check if the current user is EP
        if(!checkCurrentUserIsEntertainmentProvider()){
            view.displayError("Only Entertainment Providers can be create events.");
            return null;
        }

        //ask for event details(title, type, isTicketed)
        String title = view.getInput("Enter Event Title: ");

        //show the event type menu
        List<EventType> options = new ArrayList<>(Arrays.asList(EventType.values()));
        int choices = selectFromMenu(options, "=== [Select Event Type]===");

        EventType selectedEvent = options.get(choices - 1);

        //check if isTicketed
        String ticketed = view.getInput("Is this event ticketed? (Y/N): ");
        boolean isTicketed = ticketed.equalsIgnoreCase("Y");

        //create new Event with nextEVentID
        Event event = new Event(nextEventID, title, selectedEvent, isTicketed);
        nextEventID++;

       //ask how many performances
        int numPerformances = Integer.parseInt(view.getInput("Number of performances: "));

        //list the performances
        for (int i = 0; i < numPerformances; i++) {
            System.out.println("=== Performance " + (i + 1) + " ===");

            //get date , time and venue
            LocalDateTime startDateTime = null;
            while(startDateTime == null){
                try{
                    String startStr = view.getInput("Enter Start Date/Time (yyyy-MM-ddTHH:mm):  ");
                    startDateTime = LocalDateTime.parse(startStr);
                } catch (DateTimeParseException e){
                    view.displayError("Invalid date/time format. Please use yyyy-MM-ddTHH:mm format.");
                }
            }

            LocalDateTime endDateTime = null;
            while(endDateTime == null){
                try{
                    String endStr = view.getInput("Enter End Date/Time (yyyy-MM-ddTHH:mm):  ");
                    endDateTime = LocalDateTime.parse(endStr);
                    if(endDateTime.isBefore(startDateTime)){
                        view.displayError("End time must be after start time!");
                        endDateTime = null;
                    }
                } catch (DateTimeParseException e){
                    view.displayError("Invalid date/time format. Please use yyyy-MM-ddTHH:mm format.");
                }
            }

            //get venue
            String venueAddress = view.getInput("Enter Venue Address: ");
            int venueCapacity = Integer.parseInt(view.getInput("Enter Venue Capacity: "));
            boolean venueIsOutdoors = view.getInput("Is the venue outdoor? (Y/N) ").equalsIgnoreCase("Y");
            boolean venueAllowsSmoking = view.getInput("Is smoking allowed? (Y/N) ").equalsIgnoreCase("Y");

            //get performer names
            int numPerformers = Integer.parseInt(view.getInput("Number of performers: "));
            Collection<String> performerNames = new ArrayList<>();

            for(int j = 0; j < numPerformers; j++){
                performerNames.add(view.getInput("Enter Performance " + (j + 1) + "name: "));
            }

            //ticket details if ticketed
            int numTickets = 0;
            double ticketPrice = 0.0;

            if(isTicketed){
                numTickets = Integer.parseInt(view.getInput("Number of tickets: "));
                ticketPrice = Double.parseDouble(view.getInput("Enter Ticket Price: "));
            }

            //create performance
            Performance performance = event.createPerformance(nextPerformanceID, startDateTime, endDateTime, performerNames,
                    venueAddress, venueCapacity, venueIsOutdoors, venueAllowsSmoking, numTickets, ticketPrice);

            nextPerformanceID++;
            addPerformance(performance);
        }

        //add event to collection
        addEvent(event);
        EntertainmentProvider ep = (EntertainmentProvider) currentUser;
        ep.addEvent(event);

        view.displaySuccess("Event '" + title + "' created successfully!");
        return event;
    }

    public void searchForPerformances(){}

    public void viewPerformance(){}

    public void cancelPerformance(){}

    private boolean checkIfSponsorshipPossible(Performance performance, int amount){
        return false;
    }

    public void sponsorPerformance(){}

    private void addEvent(Event e){}

    private void addPerformance(Performance p){
        performances.add(p);
    }

    //find and return an Event by its ID
    //return null if no event with the given ID is found

    private Event getEventByID(long EventID){
        for (Event e: events){
            if(e.getEventID() == EventID){
                return e;
            }
        }
        return null;
    }


    private Event getEventByTitle(String title){
        for(Event e : events){
            if(e.getTitle().equalsIgnoreCase(title)){
                return e;
            }
        }
        return null;
    }

    private Performance getPerformanceByID(long PerformanceID){
        return null;
    }
}
