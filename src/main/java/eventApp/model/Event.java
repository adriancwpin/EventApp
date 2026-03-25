package eventApp.model;

import eventApp.enums.EventType;

import java.util.ArrayList;
import java.util.Collection;
import java.time.LocalDateTime;
import java.util.List;

public class Event {
    private long eventID;
    private String title;
    private EventType type;
    private boolean isTicketed;
    private List<Performance> performances;

    public Event(long eventID, String title, EventType type, boolean isTicketed) {
        this.eventID = eventID;
        this.title = title;
        this.type = type;
        this.isTicketed = isTicketed;
        this.performances = new ArrayList<Performance>();
    }

    public long getEventID() { return eventID; }
    public String getTitle() { return title; }
    public EventType getType() { return type; }
    public boolean isTicketed() { return isTicketed; }

    //Methods

    /**
     * Create a new Performance for this event and adds it to the performance collection list.
     *
     * @param performanceID the unique ID for this performance
     * @param startDateTime the start date and time of the performance
     * @param endDateTime the end date and time of the performance
     * @param performerNames the names of the performers
     * @param venueAddress the address of the venue
     * @param venueCapacity the maximum capacity of the venue
     * @param venueisOutdoors whether the venue is outdoors
     * @param venueAllowsSmoking whether smoking is allowed
     * @param numTickets number of tickets available
     * @param ticketPrice price of the ticket
     * @return null if a performance already exists at the same start and end times, otherwise p.
     */
    public Performance createPerformance(long performanceID, LocalDateTime startDateTime, LocalDateTime endDateTime, Collection<String> performerNames,
                                         String venueAddress, int venueCapacity, boolean venueisOutdoors, boolean venueAllowsSmoking,
                                         int numTickets, double ticketPrice ) {
        //check for time clash
        if(hasPerformanceAtSameTimes(startDateTime, endDateTime)) {
            return null;
        }

       Performance p = new Performance(performanceID, startDateTime, endDateTime, performerNames, venueAddress,
               venueCapacity, venueisOutdoors, venueAllowsSmoking, numTickets, ticketPrice);

       addPerformance(p);
       return p;
    }

    public Performance getPerformanceByID(long performanceID) {
        return null;
    }

    public Collection<String> getInfoOfPerformancesOnDate(LocalDateTime searchtDateTime) {
        Collection<String> searched = new ArrayList<>();

        for (Performance p : performances) {
            //check if performance is on the same date
            if(p.getStartDateTime().toLocalDate().equals(searchtDateTime.toLocalDate())) {
                searched.add(p.toString());
            }
        }
        return searched;
    }

    private String getOrganiserName(){
        return null;
    };

    public String getOrganiserEmail(){
        return null;
    }

    public double getAverageRatingOfPerformances(){
        return 0;
    }

    public Collection<String> getAllPerformanceReviews(){
        return null;
    }

    private boolean hasPerformanceAtSameTimes(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        for (Performance p : performances) {
            //check if existing performance has the same time slot as the previous performance
            if(p.getStartDateTime().equals(startDateTime) && p.getEndDateTime().equals(endDateTime)) {
                return true; //clash found
            }
        }
        return false;
    }

    private void addPerformance(Performance p){
    }

    public String toString() {
        return null;
    }
}
