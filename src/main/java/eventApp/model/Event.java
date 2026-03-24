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
    public Performance createPerformance(long performanceID, LocalDateTime startDateTime, LocalDateTime endDateTime, Collection<String> performerNames,
                                         String venueAddress, int venueCapacity, boolean venueisOutdoors, boolean venueAllowsSmoking, int numTickets,
                                         double ticketPrice ) {
        return null;
    }

    public Performance getPerformanceByID(long performanceID) {
        return null;
    }

    public Collection<String> getInfoOfPerformancesOnDate(LocalDateTime searchtDateTime) {
        return null;
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
        return false;
    }

    private void addPerformance(Performance p){}

    public String toString() {
        return null;
    }
}
